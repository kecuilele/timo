package com.linln.modules.elite.elitesdemand.domain;

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

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author cuike
 * @date 2019/11/18
 */
@Data
@Entity
@Table(name="elite_demandlsit")
@EntityListeners(AuditingEntityListener.class)
@Where(clause = StatusUtil.NOT_DELETE)
@Excel("demandlsit")
public class Demandlist implements Serializable {
    // 主键ID
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // UNIT ID
    @Excel("UNITID")
    private String unitid;
    // Type
    @Excel("Type")
    private String type;
    // Owner
    @Excel("Owner")
    private String owner;
    // Built Date
    @Excel("Built Date")
    private String builtdate;
    // Machinery
    @Excel("Machinery")
    private String machinery;
    // LOC Code
    @Excel("LOC Code")
    private String loccode;
    // Depot Code
    @Excel("Depot Code")
    private String depotcode;
    // Area
    @Excel("Area")
    private String area;
    // Country
    @Excel("Country")
    private String country;
    // Location
    @Excel("Location")
    private String location;
    // Condition
    @Excel("Condition")
    private String conditions;
    // Cost Price/实际成本
    @Excel("Cost Price")
    private Double costprice;
    // Selling price
    @Excel("Selling price")
    private Double sellingprice;
    // Purchaser/采购
    @Excel("Purchaser")
    private String purchaser;
    // Invoice date
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Excel("Invoicedate")
    private Date invoicedate;
    // dynamics 箱子在仓库状态 prestart 预采购 precheck 结束采购进场 preover
    private String dynamics;
    // 备注
    @Excel("Remark")
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

    //外键 订单ID
    private Long finanaceid;

    //审查日期
    private Date checkdate;

    //付款日期
    private Date paymentdate;

    //提货日期
    private Date receiveddate;
}