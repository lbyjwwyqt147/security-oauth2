package com.example.oauth.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

/***
 *  账号 DO
 */
@Entity
//@Audited   //这个注解会在数据库中创建一个历史记录表 保存历史数据
@EntityListeners(AuditingEntityListener.class)   //需要在启动类加上@EnableJpaAuditing注解 才会生效
@EqualsAndHashCode(callSuper = false)
@Data
public class SysAccount implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -6270972740177423520L;

    @Id
    @GeneratedValue
    private Long id;
    private String userAccount;
    private String userPwd;
    @CreatedDate
    private Instant createTime;
    private Long createId;
    @LastModifiedDate
    private Instant updateTime;
    private Long updateId;
    private Byte status;
    private String userEmail;
    private Integer bindingPhone;
    //上次密码重置时间
    private  Instant lastPasswordResetDate;


   /* @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL },mappedBy="account")
    private UserInfo userInfo;*/


}
