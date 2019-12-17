package com.linln.modules.elite.elitesdemand.repository;

import com.linln.modules.elite.elitesdemand.domain.Demandlist;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author cuike
 * @date 2019/11/18
 */
public interface DemandlistRepository extends BaseRepository<Demandlist, Long> , JpaSpecificationExecutor {




    @Transactional
    @Modifying
    @Query("UPDATE Demandlist e SET e.dynamics =?1,e.finanaceid = ?2,e.checkdate = ?3 WHERE e.id = ?4 ")
    void updatedemanddynamics(String precheck, Long fid, Date date, Long ids);

    Page<Demandlist> findByFinanaceid(Long id, PageRequest page);

    @Transactional
    @Modifying
    @Query("UPDATE Demandlist e SET e.dynamics =?1,e.paymentdate =?2 WHERE e.finanaceid = ?3 ")
    Integer updatepaymentdynamics(String preout, Date date, Long aLong);

    @Query("SELECT SUM(sellingprice) FROM Demandlist e WHERE e.finanaceid = ?1")
    Double getAllprice(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Demandlist e SET e.dynamics =?1 ,e.invoicedate =?2 WHERE e.finanaceid = ?3 ")
    Integer updatenopaymentdynamics(String status, Date date, Long aLong);

    List<Demandlist> findByFinanaceid(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Demandlist e SET e.dynamics =?2 ,e.receiveddate =?3 WHERE e.id = ?1 ")
    Integer billoflading(Long id, String preout, Date receiveddate);

}