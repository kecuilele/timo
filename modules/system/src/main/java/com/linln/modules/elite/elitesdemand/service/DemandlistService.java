package com.linln.modules.elite.elitesdemand.service;

import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
public interface DemandlistService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Demandlist> getPageList(Example<Demandlist> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Demandlist getById(Long id);

    /**
     * 保存数据
     * @param Demandlist 实体对象
     */
    Demandlist save(Demandlist Demandlist);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    void updatedemanddynamics(String precheck, Long fid, Long id);


    @Transactional
    Boolean updatepaymentdynamics(Long aLong,String prechecking);


    Double getAllprice(Long id);

    List<Demandlist> findByfinanceid(Long id);

    @Transactional
    Boolean billoflading(Long id, String receiveddate);

    Page<Demandlist> getByCountry(List<String> countryname);
}