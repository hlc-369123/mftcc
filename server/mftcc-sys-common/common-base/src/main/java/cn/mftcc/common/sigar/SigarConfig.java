/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.sigar;

import lombok.extern.slf4j.Slf4j;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
@RefreshScope
public class SigarConfig {

    @Value("${mftcc.sigar.path:/mftcc/sigar}")
    private String sigarPath;

    private static volatile Sigar sigar;

    //静态代码块
    /*static {
        try {
            initSigar();
        } catch (IOException e) {
            MFLogger.error(e.getMessage(),e);
        }
    }*/

    //初始化sigar的配置文件
    public Sigar getSigar() throws IOException {
        if(sigar == null){
            synchronized(SigarConfig.class){
                if(sigar == null){
                    SigarLoader loader = new SigarLoader(Sigar.class);
                    String lib = null;
                    try {
                        lib = loader.getLibraryName();
                        log.info("init sigar so文件====================="+lib);
                    } catch (ArchNotSupportedException var7) {
                        log.error("initSigar() error:{}",var7.getMessage());
                    }
                    ResourceLoader resourceLoader = new DefaultResourceLoader();
                    Resource resource = resourceLoader.getResource("classpath:/sigar.so/" + lib);
                    if (resource.exists()) {
                        File tempDir = new File(sigarPath);
                        if (!tempDir.exists()){
                            tempDir.mkdirs();
                            InputStream is = null;
                            BufferedOutputStream os = null;
                            try {
                                is = resource.getInputStream();
                                os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
                                int lentgh = 0;
                                while ((lentgh = is.read()) != -1){
                                    os.write(lentgh);
                                }
                            }catch (Exception e){
                                throw new IOException("sigar复制文件失败");
                            }finally {
                                if(os != null){
                                    os.close();
                                }
                                if(is != null){
                                    is.close();
                                }
                            }
                        }
                        System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
                        // log.info("======================org.hyperic.sigar.path:"+System.getProperty("org.hyperic.sigar.path"));
                    }
                    sigar = new Sigar();
                }
            }
        }
        return sigar;
    }
}
