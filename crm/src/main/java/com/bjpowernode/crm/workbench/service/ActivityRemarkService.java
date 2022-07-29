package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkById(String id);

    int saveActivityRemarks(ActivityRemark activityRemarks);

    int deleteActivityRemarkById(String id);

    int updateActivityRemarkByIdAndContent(ActivityRemark remark);
}
