package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> selectClueRemarkById(String id);
}
