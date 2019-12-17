package com.linln.modules.dictionary.service;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.equm.domain.Inventorys;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/12/10
 */
public interface CountrylistService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Countrylist> getPageList(Example<Countrylist> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Countrylist getById(Long id);

    /**
     * 保存数据
     * @param countrylist 实体对象
     */
    Countrylist save(Countrylist countrylist);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    void savecity(String area, String country);

    List<Countrylist> getListBySortOk();

    List<String> getCountryByCountryID(List<Long> countryID);

}