package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Activities;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int insertCreateActivity(Activities activity);

    List<Activities> queryActivityAll(Map<String,Object> map);

    int queryActivityTotalInt(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activities queryActivityByIds(String id);

    int updateActivityById(Activities activities);

    List<Activities> queryActivityAllForFile();

    List<Activities> querySelectActivityAllForFile(String[] id);

    int saveCreateActivityList(List<Activities> activitiesList);

    Activities queryActivityDetailAll(String id);

    List<Activities> selectActivitiesListForClueRemarkById(String id);

    List<Activities> selectRelaActivitiesList(Map<String,Object> map);

    List<Activities> selectClueActivityRelationByIds(String[] id);

    List<Activities> queryActivityForConvertByNameAndClueId(Map<String,Object> map);
}
