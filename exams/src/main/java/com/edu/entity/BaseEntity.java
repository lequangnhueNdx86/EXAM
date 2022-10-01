package com.edu.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@FieldNameConstants
public class BaseEntity {
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false, updatable = false)
    @CreatedDate
    private Timestamp createTime;

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = true)
    @LastModifiedDate
    private Timestamp updateTime;
    
    @PrePersist
    protected void init() {
    	this.createTime = new Timestamp(new Date().getTime());
    	this.updateTime = new Timestamp(new Date().getTime());
    }
    
    @PreUpdate
    protected void update() {
    	this.updateTime = new Timestamp(new Date().getTime());
    }
}

