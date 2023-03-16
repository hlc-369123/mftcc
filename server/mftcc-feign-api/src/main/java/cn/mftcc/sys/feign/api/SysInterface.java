package cn.mftcc.sys.feign.api;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping({"/sys"})
public interface SysInterface {
    @GetMapping({"/createToken"})
    String createToken(@RequestParam("opNo") String opNo);

    @PostMapping({"/simulationLogin"})
    String simulationLogin(@RequestParam("opNo") String opNo);

    @PostMapping({"/findUserByOpNo"})
    String findUserByOpNo(@RequestParam("opNo") String opNo);

    @PostMapping({"/findByRoleNo4MQ"})
    String findByRoleNo4MQ(@RequestParam("roleNo") String roleNo);

    @PostMapping({"/findDeptList"})
    String findDeptList();

    @PostMapping({"/findUserList"})
    String findUserList();

    @PostMapping({"/flowableWaitDemo"})
    Boolean flowableWaitDemo(@RequestBody JSONObject jsonObject);

    @PostMapping("/insertDept")
    String insertDept(@RequestBody JSONObject jsonObject);

    @PostMapping("/updateUserByOpNo")
    String updateUserByOpNo(@RequestBody JSONObject jsonObject);

    @PostMapping("/findNoDeptNoRoleUserList")
    String findNoDeptNoRoleUserList();

    @PostMapping("/getAuthByRoles")
    String getAuthByRoles(@RequestBody String[] roleArray);

    @PostMapping("/findListByRoleType")
    String findListByRoleType(@RequestBody JSONObject jsonObject);

    /**
     * 根据机构和角色查询系统用户
     * @param brNo 必填，多机构逗号分隔
     * @param roleNo 非必填，多角色逗号分隔
     * @return
     */
    @PostMapping({"/findUserListByBrRole"})
    String findUserListByBrRole(@RequestParam("brNo") String brNo,@RequestParam(name="roleNo",required = false) String roleNo);

    /**
     * 查询法人机构详细信息
     * @param corpId 法人机构编号
     * @return
     */
    @PostMapping("/findCorpCompantInfo")
    String findCorpCompantInfo(@RequestBody String corpId);
    /**
     * @Description: 根据角色编号查询所属操作员
     * @Param: [roleNo] 多个以|分割
     * @returns: java.lang.String
     * @Author  blvm
     * @Date  2021/12/15 17:24
     */
    @PostMapping({"/getUserListByRoleNo"})
    String getUserListByRoleNo(@RequestParam("roleNo") String roleNo);
    /**
     * @Description: 根据合作机构用户查询匹配到的用户信息
     * @Param: [coopUserType]
     * @returns: java.lang.String
     * @Author  blvm
     * @Date  2022/1/22 17:06
     */
    @PostMapping({"/getUserListByCoopUserType"})
    String getUserListByCoopUserType(@RequestParam("coopUserType") String coopUserType);
    /**
     * @Description: 根据当前部门编号查询上级部门信息
     * @Param: [brNo]
     * @returns: java.lang.String
     * @Author  blvm
     * @Date  2022/2/7 16:32
     */
    @PostMapping({"/findUpBrByBrNo"})
    String findUpBrByBrNo(@RequestParam("brNo") String brNo);
    /**
     * @Description: 根据当前法人机构获取法人机构名称
     * @Param: [brNo]
     * @returns: java.lang.String
     * @Author  blvm
     * @Date  2022/2/7 16:32
     */
    @PostMapping({"/findCorpName"})
    String findCorpName(@RequestParam("corpId") String corpId);

    /**
     * @Description: 根据当前部门编号查询部门信息
     * @Param: [brNo]
     * @returns: java.lang.String
     * @Author  shj
     * @Date  2022/2/24 15:32
     */
    @PostMapping({"/findBrByBrNo"})
    String findBrByBrNo(@RequestParam("brNo") String brNo);

}
