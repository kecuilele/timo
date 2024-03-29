package com.linln.modules.equm.service;

import com.linln.modules.equm.domain.Inventorys;
import com.linln.common.enums.StatusEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/07
 */
public interface InventorysService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Inventorys> getPageList(Example<Inventorys> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    Inventorys getById(Long id);

    /**
     * 保存数据
     * @param inventorys 实体对象
     */
    Inventorys save(Inventorys inventorys);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    List getCountryByUserId(Long id);

    void findText(Long id);

    Page<Inventorys> getBycountry(List<String> countryname, Inventorys example);
}