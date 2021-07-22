package com.service.rest.security.repository;

import java.util.Optional;

import com.service.rest.security.model.Role;
import com.service.rest.security.model.enums.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(UserRole rolename);
}
