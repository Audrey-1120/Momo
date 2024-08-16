package com.osundosun.momo.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CATE_T")
public class CateEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cate_seq_gen")
  @SequenceGenerator(name = "cate_seq_gen", sequenceName = "CATE_SEQ", allocationSize = 1)
  private int cateNo;
  
  @Column(name = "TOP_CATE", nullable = false)
  private int topCate;
  
  @Column(name = "PARENT_NO", nullable = true)
  private int parentNo;
  
  @Column(name = "CREATED_DATE", nullable = false)
  private Timestamp createdDate;
  
  @Column(name = "CATE_NAME", nullable = false)
  private String cateName;
  
  
}
