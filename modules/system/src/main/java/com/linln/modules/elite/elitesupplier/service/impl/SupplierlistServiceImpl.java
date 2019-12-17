package com.linln.modules.elite.elitesupplier.service.impl;

import com.linln.modules.elite.elitesupplier.domain.Supplierlist;
import com.linln.modules.elite.elitesupplier.repository.SupplierlistRepository;
import com.linln.modules.elite.elitesupplier.service.SupplierlistService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 崔珂
 * @date 2019/11/18
 */
@Service
public class SupplierlistServiceImpl implements SupplierlistService {

    @Autowired
    private SupplierlistRepository supplierlistRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Supplierlist getById(Long id) {
        return supplierlistRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Supplierlist> getPageList(Example<Supplierlist> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return supplierlistRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param supplierlist 实体对象
     */
    @Override
    public Supplierlist save(Supplierlist supplierlist) {
        return supplierlistRepository.save(supplierlist);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return supplierlistRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}