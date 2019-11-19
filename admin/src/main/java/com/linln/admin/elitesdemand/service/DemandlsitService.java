package com.linln.admin.elitesdemand.service;

import com.linln.admin.elitesdemand.domain.Demandlsit;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
public interface DemandlsitService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Demandlsit> getPageList(Example<Demandlsit> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Demandlsit getById(Long id);

    /**
     * 保存数据
     * @param demandlsit 实体对象
     */
    Demandlsit save(Demandlsit demandlsit);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}