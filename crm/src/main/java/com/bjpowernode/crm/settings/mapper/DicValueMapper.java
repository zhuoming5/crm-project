package com.bjpowernode.crm.settings.mapper;

import com.bjpowernode.crm.settings.bean.DicValue;

import java.util.List;

public interface DicValueMapper {
    /**
     * 查询数字字典类型对应的value
     * @param type
     * @return
     */
    List<DicValue> queryDicValuesByTypeCode(String type);
}
