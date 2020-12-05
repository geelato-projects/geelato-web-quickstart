package org.geelato.web.crm.m.base;

import org.geelato.core.meta.annotation.Col;
import org.geelato.core.meta.annotation.Entity;
import org.geelato.core.meta.annotation.Title;
import org.geelato.core.meta.model.entity.BaseEntity;

/**
 * @author geelato@126.com
 */
@Entity(name = "crm_contact_info")
@Title(title = "客户联系方式")
public class ContactInfo extends BaseEntity {
    private Long customerId;
    private String name;
    private String mobile;
    private String tel;
    private String area;
    private String detailAddr;
    private String description;
    private int defaultStatus;

    @Col(name = "customer_id", nullable = false)
    @Title(title = "客户ID")
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Col(name = "name", nullable = false)
    @Title(title = "联系人")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Col(name = "mobile", nullable = false)
    @Title(title = "手机")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Col(name = "tel", nullable = false)
    @Title(title = "电话")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Col(name = "area")
    @Title(title = "区域")
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Col(name = "detail_addr")
    @Title(title = "详细地址")
    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    @Col(name = "description")
    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Col(name = "default_status")
    @Title(title = "是否默认", description = "0：默认，1：非默认")
    public int getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(int defaultStatus) {
        this.defaultStatus = defaultStatus;
    }
}
