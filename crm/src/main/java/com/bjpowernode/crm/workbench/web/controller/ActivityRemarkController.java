package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ResultObject;
import com.bjpowernode.crm.commons.constans.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @ResponseBody
    @RequestMapping("/workbench/activity/saveActivityRemarks.do")
    public Object  saveActivityRemarks(String id, String noteContent, HttpSession session){
        ActivityRemark remark = new ActivityRemark();
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        remark.setId(UUIDUtils.getUUID());
        remark.setActivityId(id);
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
        remark.setNoteContent(noteContent);
        remark.setEditFlag("0");
        ResultObject reObj = new ResultObject();
        try {
            int num = activityRemarkService.saveActivityRemarks(remark);
            if (num > 0) {
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                reObj.setMsg("评论成功");
                remark.setCreateBy(user.getName());
                System.out.println(remark);
                reObj.setObj(remark);
            }else {
                reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                reObj.setMsg("评论失败");
            }
        }catch (Exception e) {
            e.printStackTrace();
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            reObj.setMsg("评论失败");
        }
        System.out.println(reObj);
        return reObj;
    }

    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public Object deleteActivityRemarkById(String id){
        ResultObject reObj = new ResultObject();
        try{
            int num = activityRemarkService.deleteActivityRemarkById(id);
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

    @ResponseBody
    @RequestMapping("/workbench/activity/saveEditActivityRemarks.do")
    public Object saveEditActivityRemarks(ActivityRemark remark,HttpSession session){
        User user = (User)session.getAttribute(Constants.SESSION_USER_KEY);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.getStringAsDateTime(new Date()));
        remark.setEditFlag("1");
        ResultObject reObj = new ResultObject();
        try {
            int num = activityRemarkService.updateActivityRemarkByIdAndContent(remark);
            if(num > 0){
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                reObj.setObj(remark);
            }else {
                reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                reObj.setMsg("系统繁忙,请稍后再试");
            }
        }catch (Exception e) {
            e.printStackTrace();
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            reObj.setMsg("系统繁忙,请稍后再试");
        }
        return reObj;
    }
}
