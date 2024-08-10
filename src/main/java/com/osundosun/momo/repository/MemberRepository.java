package com.osundosun.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osundosun.momo.entity.MemberEntity;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
  
  // 해당 유저 member_t에 있는지 조회
  MemberEntity findByEmail(String email);

}
