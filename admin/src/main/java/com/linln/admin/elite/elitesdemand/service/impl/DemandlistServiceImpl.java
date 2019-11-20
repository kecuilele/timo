package com.linln.admin.elite.elitesdemand.service.impl;

import com.linln.admin.elite.elitesdemand.repository.DemandlistRepository;
import com.linln.admin.elite.elitesdemand.service.DemandlistService;
import com.linln.admin.elitesdemand.domain.Demandlist;
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
 * @author cuike
 * @date 2019/11/18
 */
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
}