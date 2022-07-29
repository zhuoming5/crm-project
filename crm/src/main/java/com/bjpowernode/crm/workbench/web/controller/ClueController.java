package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ResultObject;
import com.bjpowernode.crm.commons.constans.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.bean.DicValue;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.Activities;
import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;
import com.bjpowernode.crm.workbench.bean.ClueRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<DicValue> appellation = dicValueService.queryDicValuesByTypeCode("appellation");
        List<DicValue> clueState = dicValueService.queryDicValuesByTypeCode("clueState");
        List<DicValue> source = dicValueService.queryDicValuesByTypeCode("source");
        List<User> users = userService.queryUsersAll();

        request.setAttribute("appellation",appellation);
        request.setAttribute("clueState",clueState);
        request.setAttribute("source",source);
        request.setAttribute("users",users);

        return "workbench/clue/index";
    }

    @ResponseBody
    @RequestMapping("/workbench/clue/saveClue.do")
    public Object saveClue(Clue clue, HttpSession session){
        User user= (User)session.getAttribute(Constants.SESSION_USER_KEY);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
        clue.setCreateBy(user.getId());
        System.out.println(clue);
        ResultObject reObj = new ResultObject();
        try {
            int num = clueService.insertClue(clue);
            if(num > 0){
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                reObj.setMsg("系统繁忙,请稍后再试");
                reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            }
        }catch (Exception e) {
            e.printStackTrace();
            reObj.setMsg("系统繁忙,请稍后再试");
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
        }
        return reObj;
    }

    @RequestMapping("/workbench/clue/clueDetail.do")
    public String ClueDetail(String id,HttpServletRequest request){

        System.out.println(id);

        Clue clue = clueService.selectClueForDetailById(id);
        List<ClueRemark> remarkList = clueRemarkService.selectClueRemarkById(id);
        List<Activities> activityList = activityService.selectActivitiesListForClueRemarkById(id);
        request.setAttribute("clue",clue);
        System.out.println(clue);
        request.setAttribute("remarkList",remarkList);
        System.out.println(remarkList);
        request.setAttribute("activityList",activityList);
        System.out.println(activityList);

        return "workbench/clue/detail";
    }

    @ResponseBody
    @RequestMapping("/workbench/clue/queryRelaActivityList.do")
    public Object queryRelaActivityList(String id,String activityName){

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("activityName",activityName);
        List<Activities> activity = activityService.selectRelaActivitiesList(map);

        return activity;
    }

    @ResponseBody
    @RequestMapping("/workbench/clue/saveActivityRelationForDetailByIds.do")
    public Object saveClueActivityRelation(String[] activityIds,String clueId){
        List<ClueActivityRelation> carList = new ArrayList<>();
        ClueActivityRelation car=null;
        for (String activityId : activityIds) {
            car = new ClueActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(activityId);
            car.setClueId(clueId);
            carList.add(car);
        }
        ResultObject reObj = new ResultObject();
        try {
            int num = clueActivityRelationService.insertClueActivityRelationById(carList);
            if(num > 0){
                List<Activities> activity = activityService.selectClueActivityRelationByIds(activityIds);
                reObj.setObj(activity);
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
        }

        return reObj;
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);
        List<DicValue> stage = dicValueService.queryDicValuesByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stage",stage);
        return "workbench/clue/convert";
    }


    @RequestMapping("/workbench/clue/queryActivityForConvertByNameAndClueId.do")
    public @ResponseBody Object queryActivityForConvertByNameAndClueId(String activityName,String clueId){
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activities> activity = activityService.queryActivityForConvertByNameAndClueId(map);
        return activity;
    }

    @RequestMapping("/workbench/clue/saveConvertClue.do")
    public @ResponseBody Object saveConvertClue(HttpSession session,String clueId,String money,String tradeName,String tradeDate,
                                  String stage,String activityId,String isCreateTransaction){
        HashMap<String, Object> map = new HashMap<>();
        User user= (User)session.getAttribute(Constants.SESSION_USER_KEY);
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("tradeName",tradeName);
        map.put("tradeDate",tradeDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTransaction",isCreateTransaction);
        map.put(Constants.SESSION_USER_KEY,user);
        ResultObject reObj = new ResultObject();
        try {
            clueService.saveClueConvert(map);
            reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            reObj.setMsg("系统繁忙,请稍后再试");
        }
        return reObj;
    }
}
