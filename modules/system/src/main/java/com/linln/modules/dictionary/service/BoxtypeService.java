package com.linln.modules.dictionary.service;

import com.linln.modules.dictionary.domain.Boxtype;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/29
 */
public interface BoxtypeService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Boxtype> getPageList(Example<Boxtype> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Boxtype getById(Long id);

    /**
     * 保存数据
     * @param boxtype 实体对象
     */
    Boxtype save(Boxtype boxtype);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}