package com.service.rest.security.service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import com.service.rest.security.exception.TokenRefreshException;
import com.service.rest.security.model.RefreshToken;
import com.service.rest.security.repository.MemberRepository;
import com.service.rest.security.repository.RefreshTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
    @Value("${spring.jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return this.refreshTokenRepository.findByToken(token);
    }

    public long removeByToken(String token) {
        return this.refreshTokenRepository.deleteByToken(token);
    }

    public RefreshToken saveByRefreshToken(RefreshToken refreshToken) {
        return this.refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiredDate().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token[" + token.getToken() + "] was expired. Please make a new signin request");
        }

        return token;
    }
    
    @Transactional
    public int deleteByUserId(Long memberId) {
        return refreshTokenRepository.deleteByMember(memberRepository.findById(memberId).get());
    }
}
