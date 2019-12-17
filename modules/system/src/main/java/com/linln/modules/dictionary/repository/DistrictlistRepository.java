package com.linln.modules.dictionary.repository;

import com.linln.modules.dictionary.domain.Districtlist;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author cuike
 * @date 2019/12/02
 */
public interface DistrictlistRepository extends BaseRepository<Districtlist, Long> {


    @Query(value="SELECT  * FROM sys_districtlist WHERE id IN (SELECT MAX(id) " +
            "FROM sys_districtlist  GROUP BY country) ", nativeQuery = true)
    List<Districtlist> findCountry();

}