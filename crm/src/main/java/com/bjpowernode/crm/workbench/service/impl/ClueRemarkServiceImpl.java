package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper cueRemarkMapper;

    @Override
    public List<ClueRemark> selectClueRemarkById(String id) {
        return cueRemarkMapper.selectClueRemarkById(id);
    }
}
