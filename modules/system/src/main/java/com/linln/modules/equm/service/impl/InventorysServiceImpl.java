package com.linln.modules.equm.service.impl;

import com.linln.modules.dictionary.repository.CountrylistRepository;
import com.linln.modules.dictionary.service.CountrylistService;
import com.linln.modules.equm.domain.Inventorys;
import com.linln.modules.equm.repository.InventorysRepository;
import com.linln.modules.equm.service.InventorysService;
import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cuike
 * @date 2019/11/07
 */
@SuppressWarnings("ALL")
@Service
public class InventorysServiceImpl implements InventorysService {

    @Autowired
    private InventorysRepository inventorysRepository;

    @Autowired
    private CountrylistRepository countrylistRepository;

    @Autowired
    private CountrylistService countrylistService;

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

    @Override
    public List getCountryByUserId(Long id) {
        return countrylistRepository.findByUserid(id);
    }

    @Override
    public void findText(Long id) {
        List<Long> countryID  = getCountryByUserId(id);
//        Countrylist countrylist = countrylistService.getById(Specification<L>);
    }

    @Override
    public Page<Inventorys> getBycountry(List<String> countryname, Inventorys example) {
        PageRequest pageRequest = PageSort.pageRequest(Sort.Direction.ASC);
        return inventorysRepository.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                Predicate result;
                for (String s : countryname) {
                    predicateList.add(criteriaBuilder.equal(root.get("country").as(String.class), s));
                }
                if (example.getUnitid() != null){
                    predicateList.add(criteriaBuilder.like(root.get("unitid"), "%"+example.getUnitid()+"%"));
                }
                if (example.getType() != null){
                    predicateList.add (criteriaBuilder.equal(root.get("type"),  example.getType()));
                }

                 result=criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()]));
                return criteriaBuilder.and(result);
            }
        }, pageRequest);
    }

}