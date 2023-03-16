/*
 * Copyright © 2020 北京微金时代科技有限公司
 * master@mftcc.cn
 */
package cn.mftcc.sys.components.demo.controller;

import cn.mftcc.common.R;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dialog")
public class DialogController {


    @PostMapping("/insertDialogCheckbox")
    public R insertDialogCheckbox(@RequestBody JSONObject demoKey1){
        System.out.println(demoKey1);
        return R.ok();
    }

    @PostMapping("/initDialogData")
    public R initDialogData(){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<10;i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value",i);
            jsonObject.put("text","选项"+i);
            jsonArray.add(jsonObject);
        }

        System.out.println(jsonArray.toJSONString());
        return R.ok().put("list",jsonArray);
    }

    @PostMapping("/insertDialogRadio")
    public R insertDialogRadio(@RequestBody JSONObject demoKey){
        System.out.println(demoKey);
        return R.ok();
    }

    @PostMapping("/insertDialogTree")
    public R insertDialogTree(@RequestBody JSONObject demoKey){
        System.out.println(demoKey);
        return R.ok();
    }

    @PostMapping("/initDialogTreeData")
    public R initDialogTreeData(){
        String cityStr = "[{\"id\":\"110000\",\"label\":\"北京市\",\"children\":[{\"id\":\"110100\",\"label\":\"市辖区\",\"children\":[{\"id\":\"110100111\",\"label\":\"东城区111\",\"children\":[]},{\"id\":\"110100222\",\"label\":\"西城区222\",\"children\":[]}]},{\"id\":\"110101\",\"label\":\"东城区\",\"children\":[]},{\"id\":\"110102\",\"label\":\"西城区\",\"children\":[]},{\"id\":\"110103\",\"label\":\"崇文区\",\"children\":[]},{\"id\":\"110104\",\"label\":\"宣武区\",\"children\":[]},{\"id\":\"110105\",\"label\":\"朝阳区\",\"children\":[]},{\"id\":\"110106\",\"label\":\"丰台区\",\"children\":[]},{\"id\":\"110107\",\"label\":\"石景山区\",\"children\":[]},{\"id\":\"110108\",\"label\":\"海淀区\",\"children\":[]},{\"id\":\"110109\",\"label\":\"门头沟区\",\"children\":[]},{\"id\":\"110111\",\"label\":\"房山区\",\"children\":[]},{\"id\":\"110112\",\"label\":\"通州区\",\"children\":[]},{\"id\":\"110113\",\"label\":\"顺义区\",\"children\":[]},{\"id\":\"110114\",\"label\":\"昌平区\",\"children\":[]},{\"id\":\"110115\",\"label\":\"大兴区\",\"children\":[]},{\"id\":\"110116\",\"label\":\"怀柔区\",\"children\":[]},{\"id\":\"110117\",\"label\":\"平谷区\",\"children\":[]},{\"id\":\"110228\",\"label\":\"密云县\",\"children\":[]},{\"id\":\"110229\",\"label\":\"延庆县\",\"children\":[]}]},{\"id\":\"120000\",\"label\":\"天津市\",\"children\":[{\"id\":\"120100\",\"label\":\"市辖区\",\"children\":[]},{\"id\":\"120101\",\"label\":\"和平区\",\"children\":[]},{\"id\":\"120102\",\"label\":\"河东区\",\"children\":[]},{\"id\":\"120103\",\"label\":\"河西区\",\"children\":[]},{\"id\":\"120104\",\"label\":\"南开区\",\"children\":[]},{\"id\":\"120105\",\"label\":\"河北区\",\"children\":[]},{\"id\":\"120106\",\"label\":\"红桥区\",\"children\":[]},{\"id\":\"120107\",\"label\":\"塘沽区\",\"children\":[]},{\"id\":\"120108\",\"label\":\"汉沽区\",\"children\":[]},{\"id\":\"120109\",\"label\":\"大港区\",\"children\":[]},{\"id\":\"120110\",\"label\":\"东丽区\",\"children\":[]},{\"id\":\"120111\",\"label\":\"西青区\",\"children\":[]},{\"id\":\"120112\",\"label\":\"津南区\",\"children\":[]},{\"id\":\"120113\",\"label\":\"北辰区\",\"children\":[]},{\"id\":\"120114\",\"label\":\"武清区\",\"children\":[]},{\"id\":\"120115\",\"label\":\"宝坻区\",\"children\":[]},{\"id\":\"120221\",\"label\":\"宁河县\",\"children\":[]},{\"id\":\"120223\",\"label\":\"静海县\",\"children\":[]},{\"id\":\"120225\",\"label\":\"蓟县\",\"children\":[]}]}]";
        JSONArray jsonArray = JSONArray.parseArray(cityStr);

        System.out.println(jsonArray.toJSONString());
        return R.ok().put("list",jsonArray);
    }


}