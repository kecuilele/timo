package com.linln.modules.equm.repository;

import com.linln.modules.equm.domain.Inventorys;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author cuike
 * @date 2019/11/07
 */
public interface InventorysRepository extends BaseRepository<Inventorys, Long> , JpaSpecificationExecutor {
//    @Query(value = "SELECT * FROM erp_inventorys WHERE country in 1? " , nativeQuery = true)
//    Page<Inventorys> findBycountry(List<String> countryname);
}