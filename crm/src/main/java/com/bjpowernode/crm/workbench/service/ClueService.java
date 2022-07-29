package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Clue;

import java.util.Map;

public interface ClueService {
    int insertClue(Clue record);

    Clue selectClueForDetailById(String id);

    void saveClueConvert(Map<String,Object> map);
}
