package com.vazgen.community.data.jpa.repository;

import com.vazgen.community.data.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  RoleEntity findByName(String name);
}
