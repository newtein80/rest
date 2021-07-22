package com.service.rest.security.service;

import javax.transaction.Transactional;

import com.service.rest.security.model.Member;
import com.service.rest.security.model.SecurityMember;
import com.service.rest.security.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// ! 중요
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // ! findByEmail로 변경 할 경우 현재의 @Override 메소드의 경우 이름이 UserDetailsService에서 정의되어있는 메소드를 구현하는 것이기때문에 메소드명을 loadUserByEmail로 변경 할 수 없다!
        Member member = this.memberRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(username + " : Member not exist.")
        );

        UserDetails checkUserDetail = SecurityMember.build(member);

        return checkUserDetail;
    }
}