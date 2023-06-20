package org.geelato.web.project.m.task;

import org.geelato.core.meta.annotation.Title;
import org.geelato.core.meta.model.entity.BaseEntity;

//@Entity(name = "prj_req")
@Title(title = "需求")
public class Requirement extends BaseEntity{
    private String title;
    private String src;
    private String priority;
    private String submitter;
    private String assignor;
    private String fixVersion;
    private String projectId;
    private String description;
    private int seq;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getAssignor() {
        return assignor;
    }

    public void setAssignor(String assignor) {
        this.assignor = assignor;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
