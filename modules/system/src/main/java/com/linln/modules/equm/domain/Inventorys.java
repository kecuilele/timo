package com.linln.modules.equm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.springframework.format.annotation.DateTimeFormat;

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
    private String receiveddate;
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
    @Excel("loccode")
    private String loccode;
    // COUNTRY
    @Excel("COUNTRY")
    private String country;
    // PORT
    @Excel("PORT")
    private String port;
    // DEPOT
    @Excel("DEPOT")
    private String depot;
    // AREA
    @Excel("AREA")
    private String area;

//    @Excel("CITY")
    private String location;
    // receiveref
    @Excel("receiveref")
    private String receiveref;
    // ContainerSTATUS
    @Excel("ContainerSTATUS")
    private String containerstatus;
    // STAYDAYS
    @Excel("STAYDAYS")
    private String staydays;
    // owner
    @Excel("owner")
    private String owner;
    // REMARKS
    @Excel("REMARKS")
    private String remarks;
    // GATE
    @Excel("GATE")
    private String gate;
    // RELEASEDATE
    @Excel("RELEASEDATE")
    private String releasedate;
    // RELEASEREF
    @Excel("RELEASEREF")
    private String releaseref;
    // GATEOUT
    @Excel("GATEOUT")
    private String gateout;
    // TRUCK
    @Excel("TRUCK")
    private String truck;
    // suggestedprice
    @Excel("suggestedprice")
    private String suggestedprice;
    // invoiceDate
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel("invoiceDate")
    private Date invoicedate;
    // INVOICE
    @Excel("INVOICE")
    private String invoice;
    // CLIENT
    @Excel("CLIENT")
    private String client;
    // SALES
    @Excel("SALES")
    private String sales;
    // paiddate
    @Excel("paiddate")
    private String paiddate;
    // ONEBILL
    @Excel("ONEBILL")
    private String onebill;
    // ONEOFFHIREDATE
    @Excel("ONEOFFHIREDATE")
    private String oneoffhiredate;
    // FreeDays
    @Excel(value = "FreeDays")
    private String freedays;
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
    private String lolo;
    // GATESERVICE
    @Excel("GATESERVICE")
    private String gateservice;
    // repaircost
    @Excel("repaircost")
    private String repaircost;
    // transport
    @Excel("transport")
    private String transport;
    // PTI
    @Excel("PTI")
    private String pti;
    // TTL
    @Excel("TTL")
    private String ttl;
    // VAT
    @Excel("VAT")
    private String vat;
    // grandttl
    @Excel("grandttl")
    private String grandttl;
    // chargemonth
    @Excel("chargemonth")
    private String chargemonth;

    private String depotcode;

    private String purchaser;

    // dynamics 箱子在仓库状态 prestart 预采购 precheck 结束采购进场 preover
    private String dynamics;
}