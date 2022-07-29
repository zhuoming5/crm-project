package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> queryActivityRemarkById(String id) {
        return activityRemarkMapper.queryActivityRemarkById(id);
    }

    @Override
    public int saveActivityRemarks(ActivityRemark activityRemarks) {
        return activityRemarkMapper.saveActivityRemarks(activityRemarks);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int updateActivityRemarkByIdAndContent(ActivityRemark remark) {
        return activityRemarkMapper.updateActivityRemarkByIdAndContent(remark);
    }
}
