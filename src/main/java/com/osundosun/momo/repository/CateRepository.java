package com.osundosun.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.CateEntity;

@Repository
public interface CateRepository extends JpaRepository<CateEntity, Integer>{

  
  
}
