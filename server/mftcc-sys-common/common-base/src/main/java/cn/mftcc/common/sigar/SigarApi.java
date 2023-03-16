/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.common.sigar;

import cn.mftcc.common.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Who;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

@RestController
@RequestMapping("/sigarApi")
@RefreshScope
public class SigarApi {

    @Value("${server.port:}")
    private String port;

    @Value("${spring.application.name:}")
    private String serverName;

    @Autowired
    private SigarConfig sigarConfig;

    private static final String UNKNOWN = "unknown";
    private static final String STATE_KEY = "state";
    @RequestMapping(value="/memory")
    public R memory() {
        try {
            Sigar sigar = sigarConfig.getSigar();
            Mem mem = sigar.getMem();
            Swap swap = sigar.getSwap();
            JSONObject memObj = new JSONObject();
            memObj.put("mem",mem);
            memObj.put("swap",swap);
            return R.ok().put("mem",memObj);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/cpu")
    public R cpu() {
        try {
            Sigar sigar = sigarConfig.getSigar();
            CpuInfo[] infos = sigar.getCpuInfoList();
            CpuPerc[] cpuList = sigar.getCpuPercList();
            double cpuTotal = 0.0;
            for (int i = 0; i < cpuList.length; i++) {// 不管是单块CPU还是多CPU都适用
                cpuTotal = SigarUtil.add(cpuTotal,cpuList[i].getCombined());
            }
            JSONObject cpu = new JSONObject();
            cpu.put("cpuInfo",infos);
            cpu.put("cpuUsed",SigarUtil.div(cpuTotal,cpuList.length));
            return R.ok().put("cpu",cpu);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/os")
    public R os() {
        try {
            OperatingSystem os = OperatingSystem.getInstance();
            return R.ok().put("os",os);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/who")
    public R who(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            Who[] who = sigar.getWhoList();
            JSONArray whoArray = new JSONArray();
            if (who != null) {
                for (int i = 0; i < who.length; i++) {
                    // log.info("当前系统进程表中的用户名" + String.valueOf(i));
                    Who who2 = who[i];
                    whoArray.add(who2.toMap());
                }
            }
            return R.ok().put("who",whoArray);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/fileSystem")
    public R file(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            JSONArray fileSystems = new JSONArray();
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                JSONObject fileSystem = new JSONObject();
                FileSystem fs = fslist[i];
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                fileSystem.put("fileSystem",fs.toMap());
                fileSystem.put("systemUsage",usage.toMap());
                fileSystems.add(fileSystem);
            }
            return R.ok().put("systemUsage",fileSystems);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/net")
    public R net() {
        try {
            Sigar sigar = sigarConfig.getSigar();
            JSONArray netInterfaces = new JSONArray();
            String ifNames[] = sigar.getNetInterfaceList();
            for (int i = 0; i < ifNames.length; i++) {
                String name = ifNames[i];
                NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
                JSONObject netInterface = new JSONObject();
                netInterface.put("name",name);
                netInterface.put("hwaddr",ifconfig.getHwaddr());
                if ((ifconfig.getFlags() & 1L) <= 0L) {
                    netInterface.put("rxBytes",UNKNOWN);
                    netInterface.put("txBytes",UNKNOWN);
                    continue;
                }
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                netInterface.put("rxBytes",ifstat.getRxBytes());
                netInterface.put("txBytes",ifstat.getTxBytes());
                netInterfaces.add(netInterface);
            }
            return R.ok().put("netInterface",netInterfaces);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/ethernet")
    public R ethernet(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            JSONArray ethernet = new JSONArray();
            String[] ifaces = sigar.getNetInterfaceList();
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress())
                        || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                        || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                ethernet.add(cfg.toMap());
            }
            return R.ok().put("ethernet",ethernet);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/serverInfo")
    public R serverInfo(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            JSONObject serverInfo = new JSONObject();
            CpuPerc[] cpuList = sigar.getCpuPercList();
            double cpuTotal = 0.0;
            for (int i = 0; i < cpuList.length; i++) {// 不管是单块CPU还是多CPU都适用
                cpuTotal = SigarUtil.add(cpuTotal,cpuList[i].getCombined());
            }
            serverInfo.put("cpu",SigarUtil.div(cpuTotal,cpuList.length));//cup使用率

            Mem mem = sigar.getMem();
            serverInfo.put("mem",SigarUtil.div(mem.getUsed(),mem.getTotal()));//物理内存使用率
            Swap swap = sigar.getSwap();
            serverInfo.put("swap",SigarUtil.div(swap.getUsed(),swap.getTotal()));//虚拟内存使用率

            FileSystem fslist[] = sigar.getFileSystemList();
            double fileTotal = 0.0;
            double fileUserd = 0.0;
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        fileTotal = SigarUtil.add(fileTotal,usage.getTotal());
                        fileUserd = SigarUtil.add(fileUserd,usage.getUsed());
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                    default:
                        break;
                }
            }
            serverInfo.put("fileSystem",SigarUtil.div(fileUserd,fileTotal));//磁盘使用率

            return R.ok().put("serverInfo",serverInfo);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/process")
    public R process(){
        try {
            Sigar sigar = new Sigar();
            JSONArray processList = new JSONArray();
            for (Long pid : sigar.getProcList()) {
                JSONObject process = new JSONObject();
                process.put("pid",pid);

                //获取cup占用情况
                try{
                    ProcCpu procCpu = sigar.getProcCpu(pid);
                    process.put("cpu",procCpu.getPercent());
                }catch (Exception e){
                    process.put("cpu",UNKNOWN);
                }
                //获取内存占用情况
                try{
                    ProcMem procMem = sigar.getProcMem(pid);
                    process.put("mem",procMem.getResident());
                    process.put("swap",procMem.getSize());
                }catch (Exception e){
                    process.put("mem",UNKNOWN);
                    process.put("swap",UNKNOWN);
                }

                //获取进程名称
                try{
                    String name = ProcUtil.getDescription(sigar, pid);
                    process.put("name",name);
                }catch (Exception e){
                    process.put("name",UNKNOWN);
                }

                //获取进程状态
                try{
                    ProcState state = sigar.getProcState(pid);
                    process.put(STATE_KEY,state.getState());
                }catch (Exception e){
                    process.put(STATE_KEY,UNKNOWN);
                }

                processList.add(process);
            }
            return R.ok().put("process",processList);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/server")
    public R server(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            JSONObject server = new JSONObject();
            CpuInfo[] cpuList = sigar.getCpuInfoList();
            server.put("cpu",cpuList[0].getModel());//cup信息

            Mem mem = sigar.getMem();
            server.put("memTotal",SigarUtil.div(mem.getTotal(),SigarUtil.mult(1024,SigarUtil.mult(1024,1024))));//内存总量
            server.put("memFree",SigarUtil.div(mem.getFree(),SigarUtil.mult(1024,SigarUtil.mult(1024,1024))));//可用内存

            OperatingSystem os = OperatingSystem.getInstance();
            server.put("osName",os.getName());//操作系统
            server.put("osArch",os.getArch());//处理器类型
            server.put("osVersion",os.getVersion());//版本号
            server.put("osDataModel",os.getDataModel());//内核版本

            FileSystem fslist[] = sigar.getFileSystemList();
            double fileTotal = 0.0;
            double fileFree = 0.0;
            for (int i = 0; i < fslist.length; i++) {
                FileSystem fs = fslist[i];
                FileSystemUsage usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        fileTotal = SigarUtil.add(fileTotal,usage.getTotal());
                        fileFree = SigarUtil.add(fileFree,usage.getFree());
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                    default:
                        break;
                }
            }
            server.put("fileTotal",SigarUtil.div(fileTotal,SigarUtil.mult(1024,1024)));//磁盘总量
            server.put("fileFree",SigarUtil.div(fileFree,SigarUtil.mult(1024,1024)));//磁盘剩余

            InetAddress addr = InetAddress.getLocalHost();
            String hostName = addr.getHostName(); //获取本机计算机名称
            server.put("hostName",hostName);//主机名

            return R.ok().put("server",server);
        }catch (Exception e){
            return R.error();
        }
    }

    @RequestMapping(value="/processBySelf")
    public R processBySelf(){
        try {
            Sigar sigar = sigarConfig.getSigar();
            String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
            String pid = runtimeName.split("@")[0];
            JSONObject process = new JSONObject();
            process.put("pid",pid);

            //获取cup占用情况
            try{
                ProcCpu procCpu = sigar.getProcCpu(pid);
                process.put("cpu",procCpu.getPercent());
            }catch (Exception e){
                process.put("cpu",UNKNOWN);
            }
            //获取内存占用情况
            try{
                ProcMem procMem = sigar.getProcMem(pid);
                process.put("mem",procMem.getResident());

                Mem mem = sigar.getMem();
                process.put("memPercent",SigarUtil.div(procMem.getResident(),mem.getTotal()));
            }catch (Exception e){
                process.put("mem",UNKNOWN);
                process.put("memPercent",UNKNOWN);
            }

            //获取进程名称
            try{
                String name = ProcUtil.getDescription(sigar, Long.valueOf(pid));
                process.put("name",name);
            }catch (Exception e){
                process.put("name",UNKNOWN);
            }

            //获取进程状态
            try{
                ProcState state = sigar.getProcState(pid);
                process.put(STATE_KEY,state.getState());
            }catch (Exception e){
                process.put(STATE_KEY,UNKNOWN);
            }

            process.put("serverName",serverName);//服务名称
            process.put("port",port);//端口

            return R.ok().put("process",process);
        }catch (Exception e){
            return R.error();
        }
    }
}
