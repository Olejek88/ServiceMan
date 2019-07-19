package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private String comment;
    private WorkStatus workStatus;
    private User author;
    private Equipment equipment;
    private TaskVerdict taskVerdict;
    private TaskTemplate taskTemplate;
    private RealmList<Operation> operations;
    private Date taskDate;
    private Date startDate;
    private Date deadlineDate;
    private Date endDate;
    private Date createdAt;
    private Date changedAt;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WorkStatus getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(WorkStatus workStatus) {
        this.workStatus = workStatus;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public TaskVerdict getTaskVerdict() {
        return taskVerdict;
    }

    public void setTaskVerdict(TaskVerdict taskVerdict) {
        this.taskVerdict = taskVerdict;
    }

    public TaskTemplate getTaskTemplate() {
        return taskTemplate;
    }

    public void setTaskTemplate(TaskTemplate taskTemplate) {
        this.taskTemplate = taskTemplate;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public RealmList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(RealmList<Operation> operations) {
        this.operations = operations;
    }

}
