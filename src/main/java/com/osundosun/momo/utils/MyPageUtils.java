package com.osundosun.momo.utils;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class MyPageUtils {

  private long total;
  // 스프링에서 지원하는 Pageable 인터페이스에서는 display 대신 size를 쓴다.
  private long size;
  private long page;
  private long begin;
  private long end;
  
  private long pagePerBlock = 6;
  private long totalPage;
  private long beginPage;
  private long endPage;
  
  public void setPaging(long total, long size, long page) {
    
    // total, size, page 값 채워주기
    this.total = total;
    this.size = size;
    this.page = page;
    
    begin = (page - 1) * size + 1;
    end = begin + size - 1;
    
    totalPage = (long) Math.ceil((double)total/size);
    
    beginPage = ((page - 1) / pagePerBlock * pagePerBlock + 1);
    endPage = Math.min(totalPage, beginPage + pagePerBlock - 1);
    
  }
  
  
  public String getPaging(String requestURI, String sort, int size) {
    
    StringBuilder builder = new StringBuilder();
    
    // <
    if(beginPage == 1) {
      builder.append("<a href=\"#\" onclick=\"return false;\" class=\"rounded\">&laquo;</a>");
    } else {
      builder.append("<a href=\"\"" + requestURI + "?page=" + (beginPage - 1) + "&sort=" + sort + "&size=" + size + "\">&laquo;</a>");
    }
    
     // 1 2 3 4 5 6
    for(long p = beginPage; p <= endPage; p++) {
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
      builder.append("<a href=\"#\"" + requestURI + "?page=" + (endPage - 1) + "&sort=" + sort + "&size=" + size + "\">&raquo;</a>");
    }
    
    return builder.toString();
  }
  
  
  
  
  
  
}
