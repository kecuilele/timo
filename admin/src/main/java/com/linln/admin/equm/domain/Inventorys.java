package com.linln.admin.equm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.modules.system.domain.User;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author cuike
 * @date 2019/11/07
 */
@Data
@Entity
@Table(name="erp_inventorys")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
@Excel("Oversea stock list")
public class Inventorys implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 备注
    private String remark;
    // 创建时间
    @CreatedDate
    private Date createDate;
    // 更新时间
    @LastModifiedDate
    private Date updateDate;
    // 创建者
    @CreatedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="create_by")
    @JsonIgnore
    private User createBy;
    // 更新者
    @LastModifiedBy
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="update_by")
    @JsonIgnore
    private User updateBy;
    // 数据状态
    private Byte status = StatusEnum.OK.getCode();
    // week
    @Excel("week")
    private String week;
    // ReceivedDate
    @Excel(value="ReceivedDate")
    @JoinColumn(name="received_date")
    private String received_date;
    // type
    @Excel("type")
    private String type;
    // unitid
    @Excel("unitid")
    private String unitid;
    // builtdate
    @Excel("builtdate")
    private String builtdate;
    // machinery
    @Excel("machinery")
    private String machinery;
    // conditions
    @Excel("conditions")
    private String conditions;
    // LOCCode
    @Excel("LOCCode")
    private String LOCCode;
    // COUNTRY
    @Excel("COUNTRY")
    private String COUNTRY;
    // PORT
    @Excel("PORT")
    private String PORT;
    // DEPOT
    @Excel("DEPOT")
    private String DEPOT;
    // AREA
    @Excel("AREA")
    private String AREA;
    // receiveref
    @Excel("receiveref")
    private String receiveref;
    // ContainerSTATUS
    @Excel("ContainerSTATUS")
    private String containerstatus;
    // STAYDAYS
    @Excel("STAYDAYS")
    private String STAYDAYS;
    // owner
    @Excel("owner")
    private String owner;
    // REMARKS
    @Excel("REMARKS")
    private String REMARKS;
    // GATE
    @Excel("GATE")
    private String GATE;
    // RELEASEDATE
    @Excel("RELEASEDATE")
    private String RELEASEDATE;
    // RELEASEREF
    @Excel("RELEASEREF")
    private String RELEASEREF;
    // GATEOUT
    @Excel("GATEOUT")
    private String GATEOUT;
    // TRUCK
    @Excel("TRUCK")
    private String TRUCK;
    // suggestedprice
    @Excel("suggestedprice")
    private String suggestedprice;
    // invoiceDate
    @Excel("invoiceDate")
    private String invoiceDate;
    // INVOICE
    @Excel("INVOICE")
    private String INVOICE;
    // CLIENT
    @Excel("CLIENT")
    private String CLIENT;
    // SALES
    @Excel("SALES")
    private String SALES;
    // paiddate
    @Excel("paiddate")
    private String paiddate;
    // ONEBILL
    @Excel("ONEBILL")
    private String ONEBILL;
    // ONEOFFHIREDATE
    @Excel("ONEOFFHIREDATE")
    private String ONEOFFHIREDATE;
    // FreeDays
    @Excel(value = "FreeDays")
    @JoinColumn(name="free_days")
    private String free_days;
    // Chargedays
    @Excel("Chargedays")
    private String chargedays;
    // rate
    @Excel("rate")
    private String rate;
    // storage
    @Excel("storage")
    private String storage;
    // LOLO
    @Excel("LOLO")
    private String LOLO;
    // GATESERVICE
    @Excel("GATESERVICE")
    private String GATESERVICE;
    // repaircost
    @Excel("repaircost")
    private String repaircost;
    // transport
    @Excel("transport")
    private String transport;
    // PTI
    @Excel("PTI")
    private String PTI;
    // TTL
    @Excel("TTL")
    private String TTL;
    // VAT
    @Excel("VAT")
    private String VAT;
    // grandttl
    @Excel("grandttl")
    private String grandttl;
    // chargemonth
    @Excel("chargemonth")
    private String chargemonth;
}