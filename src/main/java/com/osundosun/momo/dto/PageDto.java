package com.osundosun.momo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PageDto<T> {
  
  private List<T> contents;
  private int page;
  private int size;
  private long total;
  
}
