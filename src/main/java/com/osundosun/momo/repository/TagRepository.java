package com.osundosun.momo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Number>{
  
  // tagNo 여러개 받아서 가져오기
  List<TagEntity> findAllByTagNoIn(Set<Integer> tagNoList);

  
  
  
  
}
