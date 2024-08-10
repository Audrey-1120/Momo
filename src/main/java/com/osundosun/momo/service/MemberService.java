package com.osundosun.momo.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.osundosun.momo.dto.MemberDto;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
  
  // 로그인 URL 접속
  String getNaverLoginURL(HttpServletRequest request);
  // 접근 토큰 받기
  String getNaverLoginAccessToken(HttpServletRequest request);
  // 프로필 정보 받기
  MemberDto getNaverLoginProfile(String accessToken);
  // member_t에 member 데이터 있는지?
  boolean hasUser(String email);
  // 네이버 로그인!
  void naverLogin(HttpServletRequest request, MemberDto member);
  // 회원가입 - 네이버 로그인 후 회원 데이터에 데이터 없을 때
  Map<String, Object> naverSignup(MultipartHttpServletRequest multipartRequest);
  // 로그아웃
  void signout(HttpServletRequest request, HttpServletResponse response);
  
  
  
  

}
