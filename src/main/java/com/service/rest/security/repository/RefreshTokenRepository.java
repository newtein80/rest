package com.service.rest.security.repository;

import java.util.Optional;

import com.service.rest.security.model.Member;
import com.service.rest.security.model.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>{
    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    Long deleteByToken(String token);

    @Modifying // ? @Modifying ???
    int deleteByMember(Member member);

}
