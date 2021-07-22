package com.service.rest.security.repository;

import java.util.Optional;

import com.service.rest.security.model.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Use JPA to find members by username.
     * @param username Username you are looking for
     * @return Member
     */
    Optional<Member> findByUsername(String username);

}
