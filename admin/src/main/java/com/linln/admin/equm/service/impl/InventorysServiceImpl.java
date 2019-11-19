package com.linln.admin.equm.service.impl;

import com.linln.admin.equm.domain.Inventorys;
import com.linln.admin.equm.repository.InventorysRepository;
import com.linln.admin.equm.service.InventorysService;
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
 * @date 2019/11/07
 */
@Service
public class InventorysServiceImpl implements InventorysService {

    @Autowired
    private InventorysRepository inventorysRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Inventorys getById(Long id) {
        return inventorysRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Inventorys> getPageList(Example<Inventorys> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return inventorysRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param inventorys 实体对象
     */
    @Override
    public Inventorys save(Inventorys inventorys) {
        return inventorysRepository.save(inventorys);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return inventorysRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }
}