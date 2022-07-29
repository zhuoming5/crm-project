package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.Activities;
import com.bjpowernode.crm.workbench.mapper.ActivitiesMapper;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivitiesMapper activityMapper;

    @Override
    public int insertCreateActivity(Activities activity) {
        return activityMapper.insertCreateActivity(activity);
    }

    @Override
    public List<Activities> queryActivityAll(Map<String, Object> map) {
        return activityMapper.queryActivityAll(map);
    }

    @Override
    public int queryActivityTotalInt(Map<String, Object> map) {
        return activityMapper.queryActivityTotalInt(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activities queryActivityByIds(String id) {
        return activityMapper.queryActivityByIds(id);
    }

    @Override
    public int updateActivityById(Activities activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public List<Activities> queryActivityAllForFile() {
        return activityMapper.queryActivityAllForFile();
    }

    @Override
    public List<Activities> querySelectActivityAllForFile(String[] id) {
        return activityMapper.querySelectActivityAllForFile(id);
    }

    @Override
    public int saveCreateActivityList(List<Activities> activitiesList) {
        return activityMapper.saveCreateActivityList(activitiesList);
    }

    @Override
    public Activities queryActivityDetailAll(String id) {
        return activityMapper.queryActivityDetailAll(id);
    }

    @Override
    public List<Activities> selectActivitiesListForClueRemarkById(String id) {
        return activityMapper.selectActivitiesListForClueRemarkById(id);
    }

    @Override
    public List<Activities> selectRelaActivitiesList(Map<String, Object> map) {
        return activityMapper.selectRelaActivitiesList(map);
    }

    @Override
    public List<Activities> selectClueActivityRelationByIds(String[] id) {
        return activityMapper.selectClueActivityRelationByIds(id);
    }

    @Override
    public List<Activities> queryActivityForConvertByNameAndClueId(Map<String, Object> map) {
        return activityMapper.queryActivityForConvertByNameAndClueId(map);
    }

}
