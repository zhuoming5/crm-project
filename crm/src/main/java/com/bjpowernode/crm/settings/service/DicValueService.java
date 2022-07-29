package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.bean.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> queryDicValuesByTypeCode(String type);
}
