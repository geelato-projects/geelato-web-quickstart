package org.geelato.web.project.m.base;

import org.geelato.core.meta.annotation.Entity;
import org.geelato.core.meta.annotation.Title;
import org.geelato.core.meta.model.entity.BaseEntity;

/**
 * @author geemeta
 */
@Entity(name = "prj_project_info")
@Title(title = "项目")
public class ProjectInfo extends BaseEntity{

    private String name;
    private String code;
    private String ownerId;
    private String parentId;
    private String description;

    @Title(title = "名称")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Title(title = "编码")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Title(title = "所有者")
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Title(title = "父级项目")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
