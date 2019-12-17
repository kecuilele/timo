package com.linln.modules.dictionary.service.impl;

import com.linln.modules.dictionary.domain.Boxtype;
import com.linln.modules.dictionary.repository.BoxtypeRepository;
import com.linln.modules.dictionary.service.BoxtypeService;
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
 * @date 2019/11/29
 */
@Service
public class BoxtypeServiceImpl implements BoxtypeService {

    @Autowired
    private BoxtypeRepository boxtypeRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Boxtype getById(Long id) {
        return boxtypeRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Boxtype> getPageList(Example<Boxtype> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return boxtypeRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param boxtype 实体对象
     */
    @Override
    public Boxtype save(Boxtype boxtype) {
        return boxtypeRepository.save(boxtype);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return boxtypeRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}