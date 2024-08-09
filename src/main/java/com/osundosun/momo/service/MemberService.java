package com.osundosun.momo.service;

import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {
  
  String getNaverLoginURL(HttpServletRequest request);
  String getNaverLoginAccessToken(HttpServletRequest request);

}
