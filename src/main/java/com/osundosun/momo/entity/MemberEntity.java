package com.osundosun.momo.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="member_t")
public class MemberEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_gen")
  @SequenceGenerator(name = "member_seq_gen", sequenceName = "MEMBER_SEQ", allocationSize = 1)
  private int memberNo;
  
  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "PW", nullable = false)
  private String pw;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Column(name = "NICKNAME", nullable = false)
  private String nickName;

  @Column(name = "MOBILE", nullable = false)
  private String mobile;

  @Column(name = "GENDER", nullable = false)
  private String gender;

  @Column(name = "PROFILE_PATH")
  private String profilePath;

  @Column(name = "SIGNUP_KIND", nullable = false)
  private Integer signupKind;

  @CreationTimestamp
  @Column(name = "SIGNUP_DATE", nullable = false)
  private Timestamp signupDate;

  @Column(name = "TAG_NO")
  private String tagNo;
  
  @Column(name = "ROLE")
  private Integer role;
  
  public MemberEntity() {}

  public MemberEntity(String email, String password, String name, String nickname, String mobile, String gender, String profilePath, Integer signupKind, Timestamp signupDate, String tagNo) {
    this.email = email;
    this.pw = password;
    this.name = name;
    this.nickName = nickname;
    this.mobile = mobile;
    this.gender = gender;
    this.profilePath = profilePath;
    this.signupKind = signupKind;
    this.signupDate = signupDate;
    this.tagNo = tagNo;
}
  
}
