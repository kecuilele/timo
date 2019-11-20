package com.linln.admin.finanace.service.impl;

import com.linln.admin.finanace.domain.Finanaceorderss;
import com.linln.admin.finanace.repository.FinanaceorderssRepository;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.admin.finanace.service.FinanaceorderssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 崔珂
 * @date 2019/11/19
 */
@Service
public class FinanaceorderssServiceImpl implements FinanaceorderssService {

    @Autowired
    private FinanaceorderssRepository finanaceorderssRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Finanaceorderss getById(Long id) {
        return finanaceorderssRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Finanaceorderss> getPageList(Example<Finanaceorderss> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return finanaceorderssRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param finanaceorderss 实体对象
     */
    @Override
    public Finanaceorderss save(Finanaceorderss finanaceorderss) {
        return finanaceorderssRepository.save(finanaceorderss);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return finanaceorderssRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}