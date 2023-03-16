/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package net.logstash.logback.appender;

import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.net.ssl.ConfigurableSSLSocketFactory;
import ch.qos.logback.core.net.ssl.SSLConfigurableSocket;
import ch.qos.logback.core.net.ssl.SSLConfiguration;
import ch.qos.logback.core.net.ssl.SSLParametersConfiguration;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.util.CloseUtil;
import ch.qos.logback.core.util.Duration;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import net.logstash.logback.Logback11Support;
import net.logstash.logback.appender.AsyncDisruptorAppender.LogEvent;
import net.logstash.logback.appender.destination.DelegateDestinationConnectionStrategy;
import net.logstash.logback.appender.destination.DestinationConnectionStrategy;
import net.logstash.logback.appender.destination.DestinationParser;
import net.logstash.logback.appender.destination.PreferPrimaryDestinationConnectionStrategy;
import net.logstash.logback.appender.listener.TcpAppenderListener;
import net.logstash.logback.encoder.SeparatorParser;
import net.logstash.logback.encoder.com.lmax.disruptor.EventHandler;
import net.logstash.logback.encoder.com.lmax.disruptor.EventTranslatorVararg;
import net.logstash.logback.encoder.com.lmax.disruptor.LifecycleAware;

/**
 * @ClassName AbstractLogstashTcpSocketAppender
 * @Description logstash加载xml类
 *              修改line.No 126 行：去掉没配置logstash的地址时的错误打印
 *              修改line.No 639 行：去掉log错误部分
 * @Author 郭涵晨
 * @Date 2020/9/3 17:38
 */
public abstract class AbstractLogstashTcpSocketAppender<Event extends DeferredProcessingAware, Listener extends TcpAppenderListener<Event>> extends AsyncDisruptorAppender<Event, Listener> {
    protected static final String HOST_NAME_FORMAT = "%3$s";
    protected static final String PORT_FORMAT = "%4$d";
    public static final String DEFAULT_THREAD_NAME_FORMAT = "logback-appender-%1$s-%3$s:%4$d-%2$d";
    public static final int DEFAULT_PORT = 4560;
    public static final int DEFAULT_RECONNECTION_DELAY = 30000;
    public static final int DEFAULT_WRITE_TIMEOUT = 0;
    public static final int DEFAULT_QUEUE_SIZE = 8192;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    public static final int DEFAULT_WRITE_BUFFER_SIZE = 8192;
    private static final NotConnectedException NOT_CONNECTED_EXCEPTION = new NotConnectedException();
    private static final ShutdownInProgressException SHUTDOWN_IN_PROGRESS_EXCEPTION = new ShutdownInProgressException();
    private String remoteHost;
    private int port = 4560;
    private List<InetSocketAddress> destinations = new ArrayList(2);
    private volatile int connectedDestinationIndex = 0;
    private volatile InetSocketAddress connectedDestination;
    private DestinationConnectionStrategy connectionStrategy = new PreferPrimaryDestinationConnectionStrategy();
    private Duration reconnectionDelay = new Duration(30000L);
    private int acceptConnectionTimeout = 5000;
    private String peerId;
    private Encoder<Event> encoder;
    private int writeBufferSize = 8192;
    private SocketFactory socketFactory;
    private SSLConfiguration sslConfiguration;
    private Duration keepAliveDuration;
    private String keepAliveMessage = System.getProperty("line.separator");
    private Charset keepAliveCharset = Charset.forName("UTF-8");
    private byte[] keepAliveBytes;
    private Duration writeTimeout = new Duration(0L);
    private volatile CountDownLatch shutdownLatch;

    public AbstractLogstashTcpSocketAppender() {
        this.setEventHandler(new TcpSendingEventHandler());
        this.setThreadNameFormat("logback-appender-%1$s-%3$s:%4$d-%2$d");
    }

    @Override
    public boolean isStarted() {
        CountDownLatch latch = this.shutdownLatch;
        return latch != null && latch.getCount() != 0L;
    }

    @Override
    public synchronized void start() {
        if (!this.isStarted()) {
            int errorCount = 0;
            if (this.encoder == null) {
                ++errorCount;
                this.addError("No encoder was configured. Use <encoder> to specify the fully qualified class name of the encoder to use");
            }

            if (!this.destinations.isEmpty() && this.remoteHost != null) {
                ++errorCount;
                this.addError("Use '<remoteHost>/<port>' or '<destination>' but not both");
            }

            if (this.remoteHost != null) {
                this.addWarn("<remoteHost>/<port> are DEPRECATED, use <destination> instead");

                try {
                    this.addDestinations(InetSocketAddress.createUnresolved(this.remoteHost, this.port));
                } catch (IllegalArgumentException var5) {
                    ++errorCount;
                    this.addError(var5.getMessage());
                }
            }

            if (this.destinations.isEmpty()) {
                ++errorCount;
//                this.addError("No destination was configured. Use <destination> to add one or more destinations to the appender");
            }

            if (errorCount == 0 && this.socketFactory == null) {
                if (this.sslConfiguration == null) {
                    this.socketFactory = SocketFactory.getDefault();
                } else {
                    try {
                        SSLContext sslContext = this.getSsl().createContext(this);
                        SSLParametersConfiguration parameters = this.getSsl().getParameters();
                        parameters.setContext(this.getContext());
                        this.socketFactory = new UnconnectedConfigurableSSLSocketFactory(parameters, sslContext.getSocketFactory());
                    } catch (Exception var4) {
                        this.addError("Unable to create ssl context", var4);
                        ++errorCount;
                    }
                }
            }

            if (this.keepAliveMessage != null && this.keepAliveCharset != null) {
                this.keepAliveBytes = this.keepAliveMessage.getBytes(this.keepAliveCharset);
            }

            if (errorCount == 0) {
                this.encoder.setContext(this.getContext());
                if (!this.encoder.isStarted()) {
                    this.encoder.start();
                }

                int threadPoolCoreSize = this.getThreadPoolCoreSize() + 1;
                if (this.keepAliveDuration != null) {
                    ++threadPoolCoreSize;
                }

                if (this.isWriteTimeoutEnabled()) {
                    ++threadPoolCoreSize;
                }

                this.setThreadPoolCoreSize(threadPoolCoreSize);
                this.shutdownLatch = new CountDownLatch(1);
                super.start();
            }

        }
    }

    @Override
    public synchronized void stop() {
        if (this.isStarted()) {
            this.shutdownLatch.countDown();
            super.stop();
        }
    }

    protected Future<?> scheduleReaderCallable(Callable<Void> readerCallable) {
        return this.getExecutorService().submit(readerCallable);
    }

    protected void fireEventSent(Socket socket, Event event, long durationInNanos) {
        Iterator var5 = this.listeners.iterator();

        while(var5.hasNext()) {
            Listener listener = (Listener) var5.next();
            listener.eventSent(this, socket, event, durationInNanos);
        }

    }

    protected void fireEventSendFailure(Event event, Throwable reason) {
        Iterator var3 = this.listeners.iterator();

        while(var3.hasNext()) {
            Listener listener = (Listener) var3.next();
            listener.eventSendFailure(this, event, reason);
        }

    }

    protected void fireConnectionOpened(Socket socket) {
        Iterator var2 = this.listeners.iterator();

        while(var2.hasNext()) {
            Listener listener = (Listener) var2.next();
            listener.connectionOpened(this, socket);
        }

    }

    protected void fireConnectionClosed(Socket socket) {
        Iterator var2 = this.listeners.iterator();

        while(var2.hasNext()) {
            Listener listener = (Listener) var2.next();
            listener.connectionClosed(this, socket);
        }

    }

    protected void fireConnectionFailed(InetSocketAddress address, Throwable throwable) {
        Iterator var3 = this.listeners.iterator();

        while(var3.hasNext()) {
            Listener listener = (Listener) var3.next();
            listener.connectionFailed(this, address, throwable);
        }

    }

    protected Logback11Support getLogback11Support() {
        return Logback11Support.INSTANCE;
    }

    public Encoder<Event> getEncoder() {
        return this.encoder;
    }

    public void setEncoder(Encoder<Event> encoder) {
        this.encoder = encoder;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    /** @deprecated */
    @Deprecated
    public void setRemoteHost(String host) {
        this.remoteHost = host;
    }

    /** @deprecated */
    @Deprecated
    public String getRemoteHost() {
        return this.remoteHost;
    }

    /** @deprecated */
    @Deprecated
    public void setPort(int port) {
        this.port = port;
    }

    /** @deprecated */
    @Deprecated
    public int getPort() {
        return this.port;
    }

    public void addDestination(String destination) throws IllegalArgumentException {
        List<InetSocketAddress> parsedDestinations = DestinationParser.parse(destination, 4560);
        this.addDestinations((InetSocketAddress[])parsedDestinations.toArray(new InetSocketAddress[parsedDestinations.size()]));
    }

    public void addDestinations(InetSocketAddress... destinations) throws IllegalArgumentException {
        if (destinations != null) {
            InetSocketAddress[] var2 = destinations;
            int var3 = destinations.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                InetSocketAddress destination = var2[var4];

                try {
                    InetAddress.getByName(this.getHostString(destination));
                } catch (UnknownHostException var7) {
                    this.addWarn("Invalid destination '" + this.getHostString(destination) + "': host unknown (was '" + this.getHostString(destination) + "').");
                }

                this.destinations.add(destination);
            }

        }
    }

    protected String getHostString(InetSocketAddress destination) {
        return destination.getHostString();
    }

    protected void updateCurrentThreadName() {
        Thread.currentThread().setName(this.calculateThreadName());
    }

    @Override
    protected List<Object> getThreadNameFormatParams() {
        List<Object> superThreadNameFormatParams = super.getThreadNameFormatParams();
        List<Object> threadNameFormatParams = new ArrayList(superThreadNameFormatParams.size() + 2);
        threadNameFormatParams.addAll(superThreadNameFormatParams);
        InetSocketAddress currentDestination = (InetSocketAddress)this.destinations.get(this.connectedDestinationIndex);
        threadNameFormatParams.add(this.getHostString(currentDestination));
        threadNameFormatParams.add(currentDestination.getPort());
        return threadNameFormatParams;
    }

    public List<InetSocketAddress> getDestinations() {
        return Collections.unmodifiableList(this.destinations);
    }

    public void setReconnectionDelay(Duration delay) {
        if (delay != null && delay.getMilliseconds() > 0L) {
            this.reconnectionDelay = delay;
        } else {
            throw new IllegalArgumentException("reconnectionDelay must be > 0");
        }
    }

    public Duration getReconnectionDelay() {
        return this.reconnectionDelay;
    }

    public void setSecondaryConnectionTTL(Duration secondaryConnectionTTL) {
        if (this.connectionStrategy instanceof PreferPrimaryDestinationConnectionStrategy) {
            ((PreferPrimaryDestinationConnectionStrategy)this.connectionStrategy).setSecondaryConnectionTTL(secondaryConnectionTTL);
        } else {
            throw new IllegalStateException(String.format("When setting the secondaryConnectionTTL, the strategy must be a %s.  It is currently a %s", PreferPrimaryDestinationConnectionStrategy.class, this.connectionStrategy));
        }
    }

    public Duration getSecondaryConnectionTTL() {
        return this.connectionStrategy instanceof PreferPrimaryDestinationConnectionStrategy ? ((PreferPrimaryDestinationConnectionStrategy)this.connectionStrategy).getSecondaryConnectionTTL() : null;
    }

    void setAcceptConnectionTimeout(int acceptConnectionTimeout) {
        this.acceptConnectionTimeout = acceptConnectionTimeout;
    }

    public int getWriteBufferSize() {
        return this.writeBufferSize;
    }

    public void setWriteBufferSize(int writeBufferSize) {
        this.writeBufferSize = writeBufferSize;
    }

    public int getQueueSize() {
        return this.getRingBufferSize();
    }

    public void setQueueSize(int queueSize) {
        this.setRingBufferSize(queueSize);
    }

    public SSLConfiguration getSsl() {
        return this.sslConfiguration;
    }

    public void setSsl(SSLConfiguration sslConfiguration) {
        this.sslConfiguration = sslConfiguration;
    }

    public Duration getKeepAliveDuration() {
        return this.keepAliveDuration;
    }

    public void setKeepAliveDuration(Duration keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public String getKeepAliveMessage() {
        return this.keepAliveMessage;
    }

    public void setKeepAliveMessage(String keepAliveMessage) {
        this.keepAliveMessage = SeparatorParser.parseSeparator(keepAliveMessage);
    }

    public boolean isKeepAliveEnabled() {
        return this.keepAliveDuration != null && this.keepAliveMessage != null;
    }

    public boolean isWriteTimeoutEnabled() {
        return this.writeTimeout.getMilliseconds() > 0L;
    }

    public Charset getKeepAliveCharset() {
        return this.keepAliveCharset;
    }

    public void setKeepAliveCharset(Charset keepAliveCharset) {
        this.keepAliveCharset = keepAliveCharset;
    }

    @Override
    public void setThreadNameFormat(String threadNameFormat) {
        super.setThreadNameFormat(threadNameFormat);
    }

    public DestinationConnectionStrategy getConnectionStrategy() {
        return this.connectionStrategy;
    }

    @DefaultClass(DelegateDestinationConnectionStrategy.class)
    public void setConnectionStrategy(DestinationConnectionStrategy destinationConnectionStrategy) {
        this.connectionStrategy = destinationConnectionStrategy;
    }

    public Optional<InetSocketAddress> getConnectedDestination() {
        return Optional.ofNullable(this.connectedDestination);
    }

    public Duration getWriteTimeout() {
        return this.writeTimeout;
    }

    public void setWriteTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout == null ? new Duration(0L) : writeTimeout;
    }

    static {
        NOT_CONNECTED_EXCEPTION.setStackTrace(new StackTraceElement[]{new StackTraceElement(AbstractLogstashTcpSocketAppender.TcpSendingEventHandler.class.getName(), "onEvent(..)", (String)null, -1)});
        SHUTDOWN_IN_PROGRESS_EXCEPTION.setStackTrace(new StackTraceElement[]{new StackTraceElement(AbstractLogstashTcpSocketAppender.TcpSendingEventHandler.class.getName(), "onEvent(..)", (String)null, -1)});
    }

    private static class UnconnectedConfigurableSSLSocketFactory extends ConfigurableSSLSocketFactory {
        private final SSLParametersConfiguration parameters;
        private final SSLSocketFactory delegate;

        public UnconnectedConfigurableSSLSocketFactory(SSLParametersConfiguration parameters, SSLSocketFactory delegate) {
            super(parameters, delegate);
            this.parameters = parameters;
            this.delegate = delegate;
        }

        @Override
        public Socket createSocket() throws IOException {
            SSLSocket socket = (SSLSocket)this.delegate.createSocket();
            this.parameters.configure(new SSLConfigurableSocket(socket));
            return socket;
        }
    }

    private class TcpSendingEventHandler implements EventHandler<LogEvent<Event>>, LifecycleAware {
        private static final int MAX_REPEAT_CONNECTION_ERROR_LOG = 5;
        private static final int MAX_REPEAT_WRITE_ATTEMPTS = 5;
        private volatile Socket socket;
        private volatile OutputStream outputStream;
        private volatile long lastSendStartNanoTime;
        private volatile long lastSendEndNanoTime;
        private long[] destinationAttemptStartTimes;
        private ScheduledFuture<?> keepAliveFuture;
        private KeepAliveRunnable keepAliveRunnable;
        private ScheduledFuture<?> writeTimeoutFuture;
        private WriteTimeoutRunnable writeTimeoutRunnable;
        private Future<?> readerFuture;

        private TcpSendingEventHandler() {
        }

        @Override
        public void onEvent(LogEvent<Event> logEvent, long sequence, boolean endOfBatch) throws Exception {
            Exception sendFailureException = null;
            int i = 0;

            while(true) {
                if (i < 5) {
                    Socket socket = this.socket;
                    OutputStream outputStream = this.outputStream;
                    if (socket != null || AbstractLogstashTcpSocketAppender.this.isStarted() && !Thread.currentThread().isInterrupted()) {
                        Future<?> readerFuture = this.readerFuture;
                        if (!readerFuture.isDone() && socket != null) {
                            try {
                                this.writeEvent(socket, outputStream, logEvent, endOfBatch);
                                return;
                            } catch (Exception var12) {
                                sendFailureException = var12;
                                AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "unable to send event: " + var12.getMessage() + " Reconnecting.", var12);
                                this.reopenSocket();
                            }
                        } else {
                            AbstractLogstashTcpSocketAppender.this.addInfo(AbstractLogstashTcpSocketAppender.this.peerId + "destination terminated the connection. Reconnecting.");
                            this.reopenSocket();

                            try {
                                readerFuture.get();
                                sendFailureException = AbstractLogstashTcpSocketAppender.NOT_CONNECTED_EXCEPTION;
                            } catch (Exception var11) {
                                sendFailureException = var11;
                            }
                        }

                        ++i;
                        continue;
                    }

                    sendFailureException = AbstractLogstashTcpSocketAppender.SHUTDOWN_IN_PROGRESS_EXCEPTION;
                }

                if (logEvent.event != null) {
                    AbstractLogstashTcpSocketAppender.this.fireEventSendFailure((Event) logEvent.event, (Throwable)sendFailureException);
                }

                return;
            }
        }

        private void writeEvent(Socket socket, OutputStream outputStream, LogEvent<Event> logEvent, boolean endOfBatch) throws IOException {
            long startWallTime = System.currentTimeMillis();
            long startNanoTime = System.nanoTime();
            this.lastSendStartNanoTime = startNanoTime;
            if (logEvent.event != null) {
                if (AbstractLogstashTcpSocketAppender.this.getLogback11Support().isLogback11OrBefore()) {
                    AbstractLogstashTcpSocketAppender.this.getLogback11Support().doEncode(AbstractLogstashTcpSocketAppender.this.encoder, logEvent.event);
                } else {
                    outputStream.write(AbstractLogstashTcpSocketAppender.this.encoder.encode(logEvent.event));
                }
            } else if (this.hasKeepAliveDurationElapsed(this.lastSendEndNanoTime, startNanoTime)) {
                outputStream.write(AbstractLogstashTcpSocketAppender.this.keepAliveBytes);
            }

            if (endOfBatch) {
                outputStream.flush();
            }

            long endNanoTime = System.nanoTime();
            this.lastSendEndNanoTime = endNanoTime;
            if (logEvent.event != null) {
                AbstractLogstashTcpSocketAppender.this.fireEventSent(socket, (Event) logEvent.event, endNanoTime - startNanoTime);
            }

            if (AbstractLogstashTcpSocketAppender.this.connectionStrategy.shouldReconnect(startWallTime, AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex, AbstractLogstashTcpSocketAppender.this.destinations.size())) {
                AbstractLogstashTcpSocketAppender.this.addInfo(AbstractLogstashTcpSocketAppender.this.peerId + "reestablishing connection.");
                outputStream.flush();
                this.reopenSocket();
            }

        }

        private boolean hasKeepAliveDurationElapsed(long lastSentNanoTime, long currentNanoTime) {
            return AbstractLogstashTcpSocketAppender.this.isKeepAliveEnabled() && lastSentNanoTime + TimeUnit.MILLISECONDS.toNanos(AbstractLogstashTcpSocketAppender.this.keepAliveDuration.getMilliseconds()) < currentNanoTime;
        }

        @Override
        public void onStart() {
            this.destinationAttemptStartTimes = new long[AbstractLogstashTcpSocketAppender.this.destinations.size()];
            this.openSocket();
            this.scheduleKeepAlive(System.nanoTime());
            this.scheduleWriteTimeout();
        }

        @Override
        public void onShutdown() {
            this.unscheduleWriteTimeout();
            this.unscheduleKeepAlive();
            this.closeEncoder();
            this.closeSocket();
        }

        private synchronized void reopenSocket() {
            this.closeSocket();
            this.openSocket();
        }

        private synchronized void openSocket() {
            int errorCount = 0;
            int destinationIndex = AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex;

            while(AbstractLogstashTcpSocketAppender.this.isStarted() && !Thread.currentThread().isInterrupted()) {
                destinationIndex = AbstractLogstashTcpSocketAppender.this.connectionStrategy.selectNextDestinationIndex(destinationIndex, AbstractLogstashTcpSocketAppender.this.destinations.size());
                long startWallTime = System.currentTimeMillis();
                Socket tempSocket = null;
                OutputStream tempOutputStream = null;
                InetSocketAddress currentDestination = (InetSocketAddress)AbstractLogstashTcpSocketAppender.this.destinations.get(destinationIndex);

                try {
                    AbstractLogstashTcpSocketAppender.this.peerId = "Log destination " + currentDestination + ": ";
                    long millisSinceLastAttempt = startWallTime - this.destinationAttemptStartTimes[destinationIndex];
                    if (millisSinceLastAttempt < AbstractLogstashTcpSocketAppender.this.reconnectionDelay.getMilliseconds()) {
                        long sleepTime = AbstractLogstashTcpSocketAppender.this.reconnectionDelay.getMilliseconds() - millisSinceLastAttempt;
                        if (errorCount < 5 * AbstractLogstashTcpSocketAppender.this.destinations.size()) {
                            AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "Waiting " + sleepTime + "ms before attempting reconnection.");
                        }

                        try {
                            AbstractLogstashTcpSocketAppender.this.shutdownLatch.await(sleepTime, TimeUnit.MILLISECONDS);
                            if (!AbstractLogstashTcpSocketAppender.this.isStarted()) {
                                return;
                            }
                        } catch (InterruptedException var13) {
                            Thread.currentThread().interrupt();
                            AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "connection interrupted. Will no longer attempt reconnection.");
                            return;
                        }

                        startWallTime = System.currentTimeMillis();
                    }

                    this.destinationAttemptStartTimes[destinationIndex] = startWallTime;
                    tempSocket = AbstractLogstashTcpSocketAppender.this.socketFactory.createSocket();
                    tempSocket.setSoTimeout(AbstractLogstashTcpSocketAppender.this.acceptConnectionTimeout);
                    tempSocket.connect(new InetSocketAddress(AbstractLogstashTcpSocketAppender.this.getHostString(currentDestination), currentDestination.getPort()), AbstractLogstashTcpSocketAppender.this.acceptConnectionTimeout);
                    if (tempSocket instanceof SSLSocket) {
                        ((SSLSocket)tempSocket).startHandshake();
                    }

                    tempOutputStream = AbstractLogstashTcpSocketAppender.this.writeBufferSize > 0 ? new BufferedOutputStream(tempSocket.getOutputStream(), AbstractLogstashTcpSocketAppender.this.writeBufferSize) : tempSocket.getOutputStream();
                    if (AbstractLogstashTcpSocketAppender.this.getLogback11Support().isLogback11OrBefore()) {
                        AbstractLogstashTcpSocketAppender.this.getLogback11Support().init(AbstractLogstashTcpSocketAppender.this.encoder, (OutputStream)tempOutputStream);
                    }

                    AbstractLogstashTcpSocketAppender.this.addInfo(AbstractLogstashTcpSocketAppender.this.peerId + "connection established.");
                    this.socket = tempSocket;
                    this.outputStream = (OutputStream)tempOutputStream;
                    boolean shouldUpdateThreadName = destinationIndex != AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex;
                    AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex = destinationIndex;
                    AbstractLogstashTcpSocketAppender.this.connectedDestination = currentDestination;
                    AbstractLogstashTcpSocketAppender.this.connectionStrategy.connectSuccess(startWallTime, destinationIndex, AbstractLogstashTcpSocketAppender.this.destinations.size());
                    if (shouldUpdateThreadName) {
                        AbstractLogstashTcpSocketAppender.this.updateCurrentThreadName();
                    }

                    this.readerFuture = AbstractLogstashTcpSocketAppender.this.scheduleReaderCallable(new ReaderCallable(tempSocket.getInputStream()));
                    AbstractLogstashTcpSocketAppender.this.fireConnectionOpened(this.socket);
                    return;
                } catch (Exception var14) {
                    try {
                        CloseUtil.closeQuietly((Closeable)tempOutputStream);
                        CloseUtil.closeQuietly(tempSocket);
                        AbstractLogstashTcpSocketAppender.this.connectionStrategy.connectFailed(startWallTime, destinationIndex, AbstractLogstashTcpSocketAppender.this.destinations.size());
                        AbstractLogstashTcpSocketAppender.this.fireConnectionFailed(currentDestination, var14);
                        if (errorCount++ < 5 * AbstractLogstashTcpSocketAppender.this.destinations.size()) {
//                        AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "connection failed.", var14);
                            AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "connection failed.");
                        }
                    } catch (Exception e){
                        AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "connection failed.");
                    }
                }
            }

        }

        private synchronized void closeSocket() {
            AbstractLogstashTcpSocketAppender.this.connectedDestination = null;
            CloseUtil.closeQuietly(this.outputStream);
            this.outputStream = null;
            CloseUtil.closeQuietly(this.socket);
            AbstractLogstashTcpSocketAppender.this.fireConnectionClosed(this.socket);
            this.socket = null;
            if (this.readerFuture != null) {
                this.readerFuture.cancel(true);
            }

        }

        private void closeEncoder() {
            if (AbstractLogstashTcpSocketAppender.this.getLogback11Support().isLogback11OrBefore()) {
                try {
                    AbstractLogstashTcpSocketAppender.this.getLogback11Support().close(AbstractLogstashTcpSocketAppender.this.encoder);
                } catch (IOException var2) {
                    AbstractLogstashTcpSocketAppender.this.addError("Failed to close encoder", var2);
                }
            }

            AbstractLogstashTcpSocketAppender.this.encoder.stop();
        }

        private synchronized void scheduleKeepAlive(long basedOnNanoTime) {
            if (AbstractLogstashTcpSocketAppender.this.isKeepAliveEnabled() && !Thread.currentThread().isInterrupted()) {
                if (this.keepAliveRunnable == null) {
                    this.keepAliveRunnable = new KeepAliveRunnable();
                }

                long delay = TimeUnit.MILLISECONDS.toNanos(AbstractLogstashTcpSocketAppender.this.keepAliveDuration.getMilliseconds()) - (System.nanoTime() - basedOnNanoTime);

                try {
                    this.keepAliveFuture = AbstractLogstashTcpSocketAppender.this.getExecutorService().schedule(this.keepAliveRunnable, delay, TimeUnit.NANOSECONDS);
                } catch (RejectedExecutionException var6) {
                    this.keepAliveFuture = null;
                }
            }

        }

        private synchronized void unscheduleKeepAlive() {
            if (this.keepAliveFuture != null) {
                this.keepAliveFuture.cancel(true);

                try {
                    this.keepAliveFuture.get();
                } catch (InterruptedException var2) {
                    Thread.currentThread().interrupt();
                } catch (Exception var3) {
                }
            }

        }

        private synchronized void scheduleWriteTimeout() {
            if (AbstractLogstashTcpSocketAppender.this.isWriteTimeoutEnabled() && !Thread.currentThread().isInterrupted()) {
                if (this.writeTimeoutRunnable == null) {
                    this.writeTimeoutRunnable = new WriteTimeoutRunnable();
                }

                long delay = AbstractLogstashTcpSocketAppender.this.writeTimeout.getMilliseconds();

                try {
                    this.writeTimeoutFuture = AbstractLogstashTcpSocketAppender.this.getExecutorService().scheduleWithFixedDelay(this.writeTimeoutRunnable, delay, delay, TimeUnit.MILLISECONDS);
                } catch (RejectedExecutionException var4) {
                    this.writeTimeoutFuture = null;
                }
            }

        }

        private synchronized void unscheduleWriteTimeout() {
            if (this.writeTimeoutFuture != null) {
                this.writeTimeoutFuture.cancel(true);

                try {
                    this.writeTimeoutFuture.get();
                } catch (InterruptedException var2) {
                    Thread.currentThread().interrupt();
                } catch (Exception var3) {
                }
            }

        }

        private class WriteTimeoutRunnable implements Runnable {
            private volatile long lastDetectedStartNanoTime;

            private WriteTimeoutRunnable() {
            }

            public void run() {
                long lastSendStart = TcpSendingEventHandler.this.lastSendStartNanoTime;
                long lastSendEnd = TcpSendingEventHandler.this.lastSendEndNanoTime;
                if (lastSendStart > lastSendEnd && lastSendStart != this.lastDetectedStartNanoTime) {
                    long elapsedSendTimeInMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lastSendStart);
                    if (elapsedSendTimeInMillis > AbstractLogstashTcpSocketAppender.this.writeTimeout.getMilliseconds()) {
                        this.lastDetectedStartNanoTime = lastSendStart;
                        AbstractLogstashTcpSocketAppender.this.addWarn(AbstractLogstashTcpSocketAppender.this.peerId + "Detected write timeout after " + elapsedSendTimeInMillis + "ms.  Write timeout=" + AbstractLogstashTcpSocketAppender.this.getWriteTimeout() + ".  Closing socket to force reconnect");
                        TcpSendingEventHandler.this.closeSocket();
                    }
                }

            }
        }

        private class ReaderCallable implements Callable<Void> {
            private final InputStream inputStream;

            public ReaderCallable(InputStream inputStream) {
                this.inputStream = inputStream;
            }

            public Void call() throws Exception {
                AbstractLogstashTcpSocketAppender.this.updateCurrentThreadName();

                try {
                    while(true) {
                        try {
                            if (this.inputStream.read() == -1) {
                                Object var1 = null;
                                return (Void)var1;
                            }
                        } catch (SocketTimeoutException var6) {
                        } catch (Exception var7) {
                            throw var7;
                        }
                    }
                } finally {
                    if (!Thread.currentThread().isInterrupted()) {
                        AbstractLogstashTcpSocketAppender.this.getExecutorService().submit(() -> {
                            AbstractLogstashTcpSocketAppender.this.getDisruptor().getRingBuffer().tryPublishEvent((EventTranslatorVararg<LogEvent<Event>>) AbstractLogstashTcpSocketAppender.this.getEventTranslator(), (Object)null);
                        });
                    }

                }
            }
        }

        private class KeepAliveRunnable implements Runnable {
            private int previousDestinationIndex;

            private KeepAliveRunnable() {
                this.previousDestinationIndex = AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex;
            }

            public void run() {
                long lastSendEnd = TcpSendingEventHandler.this.lastSendEndNanoTime;
                long currentNanoTime = System.nanoTime();
                if (TcpSendingEventHandler.this.hasKeepAliveDurationElapsed(lastSendEnd, currentNanoTime)) {
                    AbstractLogstashTcpSocketAppender.this.getDisruptor().getRingBuffer().tryPublishEvent((EventTranslatorVararg<LogEvent<Event>>) AbstractLogstashTcpSocketAppender.this.getEventTranslator(), (Object)null);
                    TcpSendingEventHandler.this.scheduleKeepAlive(currentNanoTime);
                } else {
                    TcpSendingEventHandler.this.scheduleKeepAlive(lastSendEnd);
                }

                if (this.previousDestinationIndex != AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex) {
                    AbstractLogstashTcpSocketAppender.this.updateCurrentThreadName();
                }

                this.previousDestinationIndex = AbstractLogstashTcpSocketAppender.this.connectedDestinationIndex;
            }
        }
    }
}
