package com.linln.modules.dictionary.repository;

import com.linln.modules.dictionary.domain.Countrylist;
import com.linln.modules.equm.domain.Inventorys;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cuike
 * @date 2019/12/10
 */
public interface CountrylistRepository extends BaseRepository<Countrylist, Long> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO sys_countrylist(area,country) VALUES (?1,?2)",nativeQuery = true)
    void savecity(String area, String country);

    List<Countrylist> findAllByStatus(Sort sort, Byte code);


    @Query(value = "SELECT country_id FROM sys_user_country WHERE user_id = ?1 " , nativeQuery = true)
    List<Long> findByUserid(Long id);

    @Query(value = "SELECT country FROM sys_countrylist WHERE id in ?1 " , nativeQuery = true)
    List<String> getCountryByCountryID(List<Long> countryID);


}