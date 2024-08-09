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
public class MemberDto {

  int memberNo, signupKind, tagNo, role;
  String email, pw, name, nickname, mobile, gender, profilePath;
  Timestamp signupDate;
  
}
