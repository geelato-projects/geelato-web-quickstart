package org.geelato.web.project.m.task;

import org.geelato.core.meta.annotation.Entity;
import org.geelato.core.meta.annotation.Title;
import org.geelato.core.meta.model.entity.BaseEntity;

/**
 * @author geemeta
 */
@Entity(name = "prj_comment")
@Title(title = "评论")
public class Comment extends BaseEntity {
    private String taskId;
    private String description;

    @Title(title = "任务")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Title(title = "描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
