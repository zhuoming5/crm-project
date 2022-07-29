package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.constans.Constants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.*;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsRemarkMapper ContactsRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ClueMapper clueMapper;
    @Override
    public int insertClue(Clue record) {
        return clueMapper.insertClue(record);
    }

    @Override
    public Clue selectClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveClueConvert(Map<String, Object> map) {
        String isTran = (String) map.get("isCreateTransaction");
        String clueId = (String) map.get("clueId");
        User user = (User) map.get(Constants.SESSION_USER_KEY);

            Clue clue = clueMapper.selectClueById(clueId);
            Customer c = new Customer();
            //线索的部分内容转换到客户模块
            c.setId(UUIDUtils.getUUID());
            c.setOwner(clue.getOwner());
            c.setName(clue.getCompany());
            c.setContactSummary(clue.getContactSummary());
            c.setAddress(clue.getAddress());
            c.setCreateBy(user.getId());
            c.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
            c.setWebsite(clue.getWebsite());
            c.setPhone(clue.getPhone());
            c.setNextContactTime(clue.getNextContactTime());
            c.setDescription(clue.getDescription());
            customerMapper.insertCustomer(c);
            //线索的部分内容转换到联系人模块
            Contacts co = new Contacts();
            co.setId(UUIDUtils.getUUID());
            co.setOwner(user.getId());
            co.setSource(clue.getSource());
            co.setCustomerId(c.getId());
            co.setFullname(clue.getFullname());
            co.setAppellation(clue.getAppellation());
            co.setEmail(clue.getEmail());
            co.setMphone(clue.getMphone());
            co.setJob(clue.getJob());
            co.setCreateBy(user.getId());
            co.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
            co.setDescription(clue.getDescription());
            co.setContactSummary(clue.getContactSummary());
            co.setNextContactTime(clue.getNextContactTime());
            co.setAddress(clue.getAddress());
            contactsMapper.insertContacts(co);
            //把线索下的备注转换到客户下备注
            List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkById(clueId);
            if (crList != null && crList.size() > 0) {
                CustomerRemark cre;
                ContactsRemark con;
                List<CustomerRemark> crelist = new ArrayList<>();
                List<ContactsRemark> conlist = new ArrayList<>();
                for (ClueRemark cr : crList) {
                    cre = new CustomerRemark();
                    cre.setId(UUIDUtils.getUUID());
                    cre.setNoteContent(cr.getNoteContent());
                    cre.setCreateBy(cr.getCreateBy());
                    cre.setCreateTime(cr.getCreateTime());
                    cre.setEditBy(cr.getEditBy());
                    cre.setEditTime(cr.getEditTime());
                    cre.setEditFlag(cr.getEditFlag());
                    cre.setCustomerId(c.getId());
                    crelist.add(cre);

                    con = new ContactsRemark();
                    con.setId(UUIDUtils.getUUID());
                    con.setNoteContent(cr.getNoteContent());
                    con.setCreateBy(cr.getCreateBy());
                    con.setCreateTime(cr.getCreateTime());
                    con.setEditBy(cr.getEditBy());
                    con.setEditTime(cr.getEditTime());
                    con.setEditFlag(cr.getEditFlag());
                    con.setContactsId(co.getId());
                    conlist.add(con);
                }
                customerRemarkMapper.saveCustomerRemarkByClueRemark(crelist);
                ContactsRemarkMapper.saveContactsRemarkByClueRemark(conlist);
            }
            //把线索下的市场活动关系转到联系人和市场活动的关系
            List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
            if (carList != null && carList.size() > 0) {
                ContactsActivityRelation coare;
                ArrayList<ContactsActivityRelation> coarelist = new ArrayList<>();
                for (ClueActivityRelation clare : carList) {
                    coare = new ContactsActivityRelation();
                    coare.setId(UUIDUtils.getUUID());
                    coare.setActivityId(clare.getActivityId());
                    coare.setContactsId(co.getId());
                    coarelist.add(coare);
                }
                contactsActivityRelationMapper.saveContactsActivityRelationByList(coarelist);
            }
            //如果需要创建交易，则往交易表添加一条记录
            if ("true".equals(isTran)) {
                Tran tran = new Tran();
                tran.setId(UUIDUtils.getUUID());
                tran.setOwner(user.getId());
                tran.setMoney((String) map.get("money"));
                tran.setName((String) map.get("tradeName"));
                tran.setExpectedDate((String) map.get("tradeDate"));
                tran.setCustomerId(c.getId());
                tran.setStage((String) map.get("stage"));
                tran.setActivityId((String) map.get("activityId"));
                tran.setContactsId(co.getId());
                tran.setCreateBy(user.getId());
                tran.setCreateTime(DateUtils.getStringAsDateTime(new Date()));
                tranMapper.insertTran(tran);
                //如果需要创建交易，把线索下的备注转到交易备注表下

                if (crList != null && crList.size() > 0) {
                    List<TranRemark> trList= new ArrayList<>();
                    TranRemark tr = null;
                    for (ClueRemark cr : crList) {
                        tr = new TranRemark();
                        tr.setId(UUIDUtils.getUUID());
                        tr.setNoteContent(cr.getNoteContent());
                        tr.setCreateBy(cr.getCreateBy());
                        tr.setCreateTime(cr.getCreateTime());
                        tr.setEditBy(cr.getEditBy());
                        tr.setEditTime(cr.getEditTime());
                        tr.setEditFlag(cr.getEditFlag());
                        tr.setTranId(tran.getId());
                        trList.add(tr);
                    }
                    tranRemarkMapper.saveTranRemarkByClueRemarkList(trList);
                }

            }
            //删除线索下的所有备注
            clueRemarkMapper.deleteClueRemarkById(clueId);
            //删除线索和市场活动的关系
            clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
            //删除该线索
            clueMapper.deleteByPrimaryKey(clueId);

    }
}
