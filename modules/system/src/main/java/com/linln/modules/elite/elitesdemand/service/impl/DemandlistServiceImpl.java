package com.linln.modules.elite.elitesdemand.service.impl;

import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.elite.elitesdemand.repository.DemandlistRepository;
import com.linln.modules.elite.elitesdemand.service.DemandlistService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
@SuppressWarnings("ALL")
@Service
public class DemandlistServiceImpl implements DemandlistService {

    @Autowired
    private DemandlistRepository demandlsitRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Demandlist getById(Long id) {

        return demandlsitRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Demandlist> getPageList(Example<Demandlist> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return demandlsitRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param demandlsit 实体对象
     */
    @Override
    public Demandlist save(Demandlist demandlsit) {
        return demandlsitRepository.save(demandlsit);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return demandlsitRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public void updatedemanddynamics(String precheck, Long fid, Long id) {
        //获取当前日期作为提交审查日期
        Date date = new Date();
         demandlsitRepository.updatedemanddynamics(precheck,fid,date,id);
    }

    @Override
    @Transactional
    public Boolean updatepaymentdynamics(Long aLong,String status) {
        Date date = new Date();
        Boolean result = null;
        //增加对状态的判断更改
        if (status.equals("prenopayment")){
            result = demandlsitRepository.updatenopaymentdynamics(status,date,aLong) > 0;
        }else if (status.equals("prepayment")){
            result = demandlsitRepository.updatepaymentdynamics(status,date,aLong) > 0;
        }
        return result;
    }

    @Override
    public Double getAllprice(Long id) {
        return demandlsitRepository.getAllprice(id);
    }

    @Override
    public List<Demandlist> findByfinanceid(Long id) {
        return demandlsitRepository.findByFinanaceid(id);
    }

    @Override
    public Boolean billoflading(Long id, String receiveddate) {
        String preout = "preout";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(receiveddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return demandlsitRepository.billoflading(id,preout,date) > 0;
    }

    @Override
    public Page<Demandlist> getByCountry(List<String> countryname) {

        PageRequest pageRequest = PageSort.pageRequest(Sort.Direction.ASC);
        return demandlsitRepository.findAll(new Specification(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                for (String s : countryname) {
                    predicateList.add(criteriaBuilder.equal(root.get("country").as(String.class), s));
                }
                return criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        },pageRequest);
    }


}