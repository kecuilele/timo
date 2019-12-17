package com.linln.modules.finanace.service;

import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.finanace.domain.Finanaceorderss;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 崔珂
 * @date 2019/11/19
 */
public interface FinanaceorderssService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Finanaceorderss> getPageList(Example<Finanaceorderss> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Finanaceorderss getById(Long id);

    /**
     * 保存数据
     * @param finanaceorderss 实体对象
     */
    Finanaceorderss save(Finanaceorderss finanaceorderss);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    Page<Demandlist> getdemanddetail(Long id);


    String selectOrderStatus(Long id);

    void updateOrderstatusByid(String purchasestatus, Long id);
}