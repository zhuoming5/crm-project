package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ResultObject;
import com.bjpowernode.crm.commons.constans.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.HSSFUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.Activities;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;


@Controller
public class ActivityController {

    @Autowired
    private ActivityRemarkService activityRemarkService;
    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryUsersAll();
        request.setAttribute("users",users);
        return "workbench/activity/index";
    }

    @ResponseBody
    @RequestMapping(value = "/workbench/activity/saveActivity.do" ,method = RequestMethod.POST)
    public Object saveActivity(Activities activity, HttpSession session){
        System.out.println(activity);
        ResultObject obj = new ResultObject();
        User user = (User)session.getAttribute(Constants.SESSION_USER_KEY);
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
        activity.setId(UUIDUtils.getUUID());
        try {
            int num = activityService.insertCreateActivity(activity);
            if(num > 0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMsg("系统繁忙,请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMsg("系统繁忙,请稍后再试");
        }
        return obj;
    }

    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityAll.do")
    public Object queryActivityAll(String name,String owner,
                                   String startDate,String endDate,Integer pageNo,Integer pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        List<Activities> activities = activityService.queryActivityAll(map);
        int totalRows = activityService.queryActivityTotalInt(map);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("activities",activities);
        resultMap.put("totalRows",totalRows);
        return resultMap;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    public Object deleteActivityByIds(String[] id){

        ResultObject obj = new ResultObject();
        try{
            int num = activityService.deleteActivityByIds(id);
            if(num > 0){
                obj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                obj.setMsg("系统繁忙,请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            obj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            obj.setMsg("系统繁忙,请稍后再试");
        }
        return obj;
    }

    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityById.do")
    public Object queryActivityByIds(String id){
        return activityService.queryActivityByIds(id);
    }


    @ResponseBody
    @RequestMapping(value = "/workbench/activity/updateActicityById.do",method = RequestMethod.POST)
    public Object updateActivityById(Activities activity,HttpSession session){
        User user= (User) session.getAttribute(Constants.SESSION_USER_KEY);
        activity.setEditTime(DateUtils.getStringAsDateTime(new Date()));
        activity.setEditBy(user.getId());
        ResultObject reObj = new ResultObject();
        try {
            int num = activityService.updateActivityById(activity);
            if(num > 0){
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                reObj.setMsg("系统繁忙,请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            reObj.setMsg("系统繁忙,请稍后再试");
        }
        return reObj;
    }

    @RequestMapping("/workbench/activity/FileDownload.do")
    public void FileDownload(HttpServletResponse response) throws IOException {

        List<Activities> activity = activityService.queryActivityAllForFile();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow(0);//行

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名字");
        cell=row.createCell(3);
        cell.setCellValue("开始时间");
        cell=row.createCell(4);
        cell.setCellValue("结束时间");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");

        HSSFRow row1;
        for (int i = 0; i < activity.size();i++) {
            Activities activities = activity.get(i);
            row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(activities.getId());
            cell=row1.createCell(1);
            cell.setCellValue(activities.getOwner());
            cell=row1.createCell(2);
            cell.setCellValue(activities.getName());
            cell=row1.createCell(3);
            cell.setCellValue(activities.getStartDate());
            cell=row1.createCell(4);
            cell.setCellValue(activities.getEndDate());
            cell=row1.createCell(5);
            cell.setCellValue(activities.getCost());
            cell=row1.createCell(6);
            cell.setCellValue(activities.getDescription());
            cell=row1.createCell(7);
            cell.setCellValue(activities.getCreateTime());
            cell=row1.createCell(8);
            cell.setCellValue(activities.getCreateBy());
            cell=row1.createCell(9);
            cell.setCellValue(activities.getEditTime());
            cell=row1.createCell(10);
            cell.setCellValue(activities.getEditBy());
        }

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activity.xls");
        OutputStream out = response.getOutputStream();

        wb.write(out);
        out.flush();
        wb.close();

    }

    @RequestMapping("/workbench/activity/selectFileDownload.do")
    public void selectFileDownload(String[] id, HttpServletResponse response) throws IOException {
        List<Activities> activity = activityService.querySelectActivityAllForFile(id);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow(0);//行

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("id");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名字");
        cell=row.createCell(3);
        cell.setCellValue("开始时间");
        cell=row.createCell(4);
        cell.setCellValue("结束时间");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");

        HSSFRow row1;
        for (int i = 0; i < activity.size();i++) {
            Activities activities = activity.get(i);
            row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(activities.getId());
            cell=row1.createCell(1);
            cell.setCellValue(activities.getOwner());
            cell=row1.createCell(2);
            cell.setCellValue(activities.getName());
            cell=row1.createCell(3);
            cell.setCellValue(activities.getStartDate());
            cell=row1.createCell(4);
            cell.setCellValue(activities.getEndDate());
            cell=row1.createCell(5);
            cell.setCellValue(activities.getCost());
            cell=row1.createCell(6);
            cell.setCellValue(activities.getDescription());
            cell=row1.createCell(7);
            cell.setCellValue(activities.getCreateTime());
            cell=row1.createCell(8);
            cell.setCellValue(activities.getCreateBy());
            cell=row1.createCell(9);
            cell.setCellValue(activities.getEditTime());
            cell=row1.createCell(10);
            cell.setCellValue(activities.getEditBy());
        }

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activity.xls");
        OutputStream out = response.getOutputStream();

        wb.write(out);
        out.flush();
        wb.close();
    }

    @ResponseBody
    @RequestMapping("/workbench/activity/importActivityList.do")
    public Object importActivityList(MultipartFile file, HttpSession session){
//        String originalFilename = file.getOriginalFilename();
        ResultObject reObj = new ResultObject();
        try {
//            File file1 = new File("D:\\Software\\Java\\ideaHome\\crm-project\\crm\\src\\main\\java\\com\\bjpowernode\\crm\\file\\",originalFilename);
//            file.transferTo(file1);
//            FileInputStream is = new FileInputStream("D:\\Software\\Java\\ideaHome\\crm-project\\crm\\src\\main\\java\\com\\bjpowernode\\crm\\file\\"+originalFilename);
            InputStream is = file.getInputStream();
            HSSFWorkbook sheets = new HSSFWorkbook(is);//获取第一页
            HSSFSheet sheetAt = sheets.getSheetAt(0);//获取第一行
            HSSFRow row = null;
            HSSFCell cell = null;
            Activities activities = null;
            List<Activities> activityList = new ArrayList<>();
            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {//循环每行的数据 最后行的下标
                row = sheetAt.getRow(i);
                activities = new Activities();
                activities.setId(UUIDUtils.getUUID());
                activities.setOwner(((User)session.getAttribute(Constants.SESSION_USER_KEY)).getId());
                activities.setCreateBy(((User)session.getAttribute(Constants.SESSION_USER_KEY)).getId());
                activities.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
                for (int j = 0; j < row.getLastCellNum(); j++) {//循环每列的数据 最后单元格的下标+1
                    cell = row.getCell(j);
                    String cellValueForStr = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                        activities.setName(cellValueForStr);
                    }else if (j == 1){
                        activities.setStartDate(cellValueForStr);
                    }else if (j == 2){
                        activities.setEndDate(cellValueForStr);
                    }else if (j == 3){
                        activities.setCost(cellValueForStr);
                    }else if (j == 4){
                        activities.setDescription(cellValueForStr);
                    }
                }
                activityList.add(activities);
            }


            int num = activityService.saveCreateActivityList(activityList);
            System.out.println("-------------->:"+num);
                reObj.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                reObj.setObj(num);

        }catch (Exception e) {
            e.printStackTrace();
            reObj.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            reObj.setMsg("上传失败");
        }
        return reObj;
    }

    @RequestMapping("/workbench/activity/toActivityDetail.do")
    public String toActivityDetail(String id,HttpServletRequest request){
        Activities activity = activityService.queryActivityDetailAll(id);
        List<ActivityRemark> activityRemarksList = activityRemarkService.queryActivityRemarkById(id);

        request.setAttribute("activity",activity);
        request.setAttribute("activityRemarksList",activityRemarksList);

        return "workbench/activity/detail";
    }
}
