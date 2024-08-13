package com.osundosun.momo.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CateDto {

  int cateNo, topCate, parentNo;
  String cateName;
  Timestamp createdDate;
  
}
