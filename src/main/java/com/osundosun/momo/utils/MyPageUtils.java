package com.osundosun.momo.utils;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class MyPageUtils {

  private int total;
  // 스프링에서 지원하는 Pageable 인터페이스에서는 display 대신 size를 쓴다.
  private int size;
  private int page;
  private int begin;
  private int end;
  
  private int pagePerBlock = 6;
  private int totalPage;
  private int beginPage;
  private int endPage;
  
  public String getPaging(String requestURI, String sort, int size) {
    
    StringBuilder builder = new StringBuilder();
    
    // <
    if(beginPage == 1) {
      builder.append("<a href=\"#\" onclick=\"return false;\" class=\"rounded\">&laquo;</a>");
    } else {
      builder.append("<a href=\"#\" class=\"active rounded\">1</a>");
    }
     // 1 2 3 4 5 6
    for(int p = beginPage; p <= endPage; p++) {
      if(p == page) {
        builder.append("<a href=\"" + requestURI + "/?page=" + p + "&sort=" + sort + "&size=" + size + "\" class=\"active rounded\">" + p + "</a>");
      } else {
        builder.append("<a href=\"" + requestURI + "/?page=" + p + "&sort=" + sort + "&size=" + size + "\">" + p + "</a>");
      }
    }
    
    // >
    if(endPage == totalPage) {
      builder.append("<a href=\"#\" onclick=\"return false;\" class=\"rounded\">&raquo;</a>");
    } else {
      
    }
    
    return builder.toString();
  }
  
  
  
  
  
  
}
