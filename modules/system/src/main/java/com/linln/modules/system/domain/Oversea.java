package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.component.excel.enums.ExcelType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "oversea")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update oversea" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
public class Oversea implements Serializable {

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\downloads\\Oversea stock list.xlsx"));
        List<Oversea> overseas = ExcelUtil.importExcel(Oversea.class, fileInputStream);
        System.out.println();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Excel("WEEK")
    private String WEEK;
    @Excel("RECEIVEDDATE")
    private String RECEIVEDDATE;
    @Excel("TYPE")
    private String TYPE;
    @Excel("UNITID")
    private String UNITID;
    @Excel("BUILTDATE")
    private String BUILTDATE;
    @Excel("MACHINERY")
    private String MACHINERY;
    @Excel("CONDITION")
    private String CONDITION;
    @Excel("LOCCODE")
    private String LOCCODE;
    @Excel("COUNTRYAREA")
    private String COUNTRYAREA;
    @Excel("PORT")
    private String PORT;
    @Excel("DEPOT")
    private String DEPOT;
    @Excel("AREA")
    private String AREA;
    @Excel("RECEIVEREF")
    private String RECEIVEREF;
    @Excel("STATUS")
    private String STATUS;
    @Excel("STAYDAYS")
    private String STAYDAYS;
    @Excel("OWNER")
    private String OWNER;
    @Excel("REMARKS")
    private String REMARKS;
    @Excel("GATEIN")
    private String GATEIN;
    @Excel("RELEASEDATE")
    private String RELEASEDATE;
    @Excel("RELEASEREF")
    private String RELEASEREF;
    @Excel("GATEOUT")
    private String GATEOUT;
    @Excel("TRUCK")
    private String TRUCK;
    @Excel("SUGGESTEDPRICE")
    private String SUGGESTEDPRICE;
    @Excel("INVOICEDATE")
    private String INVOICEDATE;
    @Excel("INVOICE")
    private String INVOICE;
    @Excel("CLIENT")
    private String CLIENT;
    @Excel("SALES")
    private String SALES;
    @Excel("PAIDDATE")
    private String PAIDDATE;
    @Excel("ONEBILL")
    private String ONEBILL;
    @Excel("ONEOFFHIREDATE")
    private String ONEOFFHIREDATE;
    @Excel("FREEDAYS")
    private String FREEDAYS;
    @Excel("CHARGEDAYS")
    private String CHARGEDAYS;
    @Excel("RATE")
    private String RATE;
    @Excel("STORAGE")
    private String STORAGE;
    @Excel("LOLO")
    private String LOLO;
    @Excel("GATESERVICE")
    private String GATESERVICE;
    @Excel("REPAIRCOST")
    private String REPAIRCOST;
    @Excel("TRANSPORT")
    private String TRANSPORT;
    @Excel("PTI")
    private String PTI;
    @Excel("TTL")
    private String TTL;
    @Excel("VAT")
    private String VAT;
    @Excel("GRANDTTL")
    private String GRANDTTL;
    @Excel("CHARGEMONTH")
    private String CHARGEMONTH;
}
