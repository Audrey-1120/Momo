package com.osundosun.momo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osundosun.momo.entity.MemberEntity;

public interface AdminUserRepository extends JpaRepository<MemberEntity, Integer>{
  
  // 해당 유저 객체 있는지 조회
  MemberEntity findByEmailAndPwAndRole(String email, String pw, Integer role);
}
