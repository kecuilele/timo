package com.linln.modules.dictionary.service.impl;

import com.linln.modules.dictionary.domain.Districtlist;
import com.linln.modules.dictionary.repository.DistrictlistRepository;
import com.linln.modules.dictionary.service.DistrictlistService;
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
 * @date 2019/12/02
 */
@SuppressWarnings("ALL")
@Service
public class DistrictlistServiceImpl implements DistrictlistService {

    @Autowired
    private DistrictlistRepository districtlistRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Districtlist getById(Long id) {
        return districtlistRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Districtlist> getPageList(Example<Districtlist> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return districtlistRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param districtlist 实体对象
     */
    @Override
    public Districtlist save(Districtlist districtlist) {
        return districtlistRepository.save(districtlist);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return districtlistRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<Districtlist> findCountry() {
        return districtlistRepository.findCountry();
    }

    @Override
    public List<Districtlist> findAll() {
        return districtlistRepository.findAll();
    }
}