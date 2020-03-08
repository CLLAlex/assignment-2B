package com.via.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Indexed
public class Product implements Serializable {

    public enum FormStatus{REJECTED , PENDING, ACCEPTED};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @CsvBindByName(column = "id")
    @DocumentId(name="id")
    @CsvBindByPosition(position = 0)
    private Integer id;

    @Field(termVector = TermVector.YES)
    @CsvBindByName(column = "borrowerName")
    @CsvBindByPosition(position = 1)
    private String borrowerName;

    @Field(termVector = TermVector.YES)
    @CsvBindByName(column = "email")
    @CsvBindByPosition(position = 2)
    private String email;

    @Field(termVector = TermVector.YES)
    @CsvBindByName(column = "phoneNum")
    @CsvBindByPosition(position = 3)
    private String phoneNum;


    @Field( analyze= Analyze.NO ,termVector = TermVector.YES, bridge=@FieldBridge(impl= EnumBridge.class))
    @Enumerated(EnumType.STRING)
    @CsvBindByName(column = "status")
    @CsvBindByPosition(position = 5)
    private FormStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount user;

    private String rejectReason;

    @CsvBindByPosition(position = 4)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    private Date updateTime;

    @PrePersist
    public void createAction(){
        createTime = new Date();
        updateTime = new Date();
    }

    @PreUpdate
    public void updateAction(){
        updateTime = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public FormStatus getStatus() {
        return status;
    }

    public void setStatus(FormStatus status) {
        this.status = status;
    }

    public UserAccount getUser() {
        return user;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
