package org.geelato.web.crm.m.base;

import org.geelato.core.meta.annotation.Col;
import org.geelato.core.meta.annotation.Entity;
import org.geelato.core.meta.annotation.Title;
import org.geelato.core.meta.model.entity.BaseEntity;
import java.util.Date;
/**
 * @author geelato@126.com
 */
@Entity(name = "crm_customer")
@Title(title = "客户")
public class Customer extends BaseEntity {
    private String name;

    private String type;

    private String officeAddress;

    private String contactName;

    private String contactWay;

    private String socialCreditCode;

    private Date registrationDate;

    private String registeredCapital;

    private String registeredAddress;

    private int level;

    private Long salesmanId;

    private String salesmanName;
    private String description;

    @Col(name = "name",nullable = false)
    @Title(title = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Col(name = "type")
    @Title(title = "类型")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Col(name = "office_address")
    @Title(title = "办公地址")
    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    @Col(name = "contact_name")
    @Title(title = "联系人")
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Col(name = "contact_way")
    @Title(title = "联系方式")
    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    @Col(name = "social_credit_code")
    @Title(title = "社会信用代码")
    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    @Col(name = "registration_date")
    @Title(title = "注册时间")
    public Date getRegistrationDate() {
        return registrationDate;
    }


    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Col(name = "registration_capital")
    @Title(title = "注册资金")
    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    @Col(name = "registration_address")
    @Title(title = "注册地址")
    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    @Col(name = "level")
    @Title(title = "级别")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Col(name = "salesman_id")
    @Title(title = "销售ID")
    public Long getSalesmanId() {
        return salesmanId;
    }

    public void setSalesmanId(Long salesmanId) {
        this.salesmanId = salesmanId;
    }
    @Col(name = "salesman_name")
    @Title(title = "销售名称")
    public String getSalesmanName() {
        return salesmanName;
    }

    @Col(name = "description")
    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
