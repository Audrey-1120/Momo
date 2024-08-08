package com.osundosun.momo.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


public interface AdminUserService {
  
  Map<String, Object> login(HttpServletRequest request);

}
