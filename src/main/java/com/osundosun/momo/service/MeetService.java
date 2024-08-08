package com.osundosun.momo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.osundosun.momo.jpa.TagRepository;

@Service
public class MeetService {

  @Autowired
  private TagRepository tagRepository;
  
  public ResponseEntity<Map<String, Object>> getTagList() {
    return new ResponseEntity<>(Map.of("tagList", tagRepository.findAll()), HttpStatus.OK);
}
  
  
}
