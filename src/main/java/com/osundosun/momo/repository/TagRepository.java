package com.osundosun.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Number>{

  
  
  
  
}
