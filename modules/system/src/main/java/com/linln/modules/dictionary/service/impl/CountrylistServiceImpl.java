package com.linln.modules.dictionary.service.impl;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.modules.dictionary.repository.CountrylistRepository;
import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.equm.domain.Inventorys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/12/10
 */
@SuppressWarnings("ALL")
@Service
public class CountrylistServiceImpl implements CountrylistService {

    @Autowired
    private CountrylistRepository countrylistRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public Countrylist getById(Long id) {
        return countrylistRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Countrylist> getPageList(Example<Countrylist> example) {
        // 创建分页对象
//        Sort sort = new Sort(Sort.Direction.ASC,"area");
        PageRequest page = PageSort.areapageRequest();
        return countrylistRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param countrylist 实体对象
     */
    @Override
    public Countrylist save(Countrylist countrylist) {
        return countrylistRepository.save(countrylist);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return countrylistRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public void savecity(String area, String country) {
        countrylistRepository.savecity(area,country);
    }

    @Override
    public List<Countrylist> getListBySortOk() {
        Sort sort = new Sort(Sort.Direction.ASC, "createDate");
        return countrylistRepository.findAllByStatus(sort, StatusEnum.OK.getCode());
    }

    @Override
    public List<String> getCountryByCountryID(List<Long> countryID) {
        return countrylistRepository.getCountryByCountryID(countryID);
    }



}