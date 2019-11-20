package com.linln.admin.elite.admin.elitesupplier.service;

import com.linln.admin.elite.admin.elitesupplier.domain.Supplierlist;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 崔珂
 * @date 2019/11/18
 */
public interface SupplierlistService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Supplierlist> getPageList(Example<Supplierlist> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Supplierlist getById(Long id);

    /**
     * 保存数据
     * @param supplierlist 实体对象
     */
    Supplierlist save(Supplierlist supplierlist);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}