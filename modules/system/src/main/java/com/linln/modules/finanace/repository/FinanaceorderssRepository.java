package com.linln.modules.finanace.repository;

import com.linln.modules.finanace.domain.Finanaceorderss;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author 崔珂
 * @date 2019/11/19
 */
public interface FinanaceorderssRepository extends BaseRepository<Finanaceorderss, Long> {


    @Transactional
    @Modifying
    @Query("UPDATE Finanaceorderss f SET f.orderstatus = ?1 , f.paymentdate = ?2 where f.id=?3")
    void updateOrderstatusByid(String payment, Date date, Long id);


    @Transactional
    @Modifying
    @Query("UPDATE Finanaceorderss f SET f.orderstatus = ?1 , f.checkdate = ?2 where f.id=?3")
    void updateOrderstatuspaymentByid(String purchasestatus, Date date, Long id);
}