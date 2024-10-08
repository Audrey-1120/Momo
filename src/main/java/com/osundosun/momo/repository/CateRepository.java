package com.osundosun.momo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.CateEntity;

@Repository
public interface CateRepository extends JpaRepository<CateEntity, Integer>{

  // cateNo 여러개 받아서 가져오기
  List<CateEntity> findAllByCateNoIn(Set<Long> cateNoList);
  
}
