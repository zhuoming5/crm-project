package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * 通过id查询activity的留言备注（一对多）
     * @param id
     * @return
     */
    List<ActivityRemark> queryActivityRemarkById(String id);

    /**
     * 保存市场活动备注
     * @param activityRemarks
     * @return
     */
    int saveActivityRemarks(ActivityRemark activityRemarks);

    /**
     * 删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    int updateActivityRemarkByIdAndContent(ActivityRemark remark);
}
