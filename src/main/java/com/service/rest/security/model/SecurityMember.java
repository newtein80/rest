package com.service.rest.security.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityMember implements UserDetails {
    private static final long serialVersionUiD = 1L;

    private Long id;

    private String username;

    private String email;
    
    @JsonIgnore // ! response 될 때 무시하는 필드 설정
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * @param id
     * @param username
     * @param email
     * @param password
     * @param authorities
     */
    public SecurityMember(Long id, String username, String email, String password,
            Collection<? extends GrantedAuthority> authorities) {
        // ! 사용자가 설정한 custom한 role을 spring.security에서 관리하는 권한 객체로 변경하는 것 // 접근제어 개념 - ? extends GrantedAuthority -> 코드 작성할때부터(컴파일될때) 빨간줄
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static SecurityMember build(Member member) {
        List<GrantedAuthority> authorities = member.getRoles().stream().map(
            role -> new SimpleGrantedAuthority(role.getName().name())
        )
        .collect(Collectors.toList());

        // super(member.getUsername(), "{noop}"+ member.getPassword(), AuthorityUtils.createAuthorityList(member.getRole().toString())); 를 변형
        return new SecurityMember(member.getId(), member.getUsername(), member.getEmail(), member.getPassword(), authorities);
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
            SecurityMember user = (SecurityMember) o;
		return Objects.equals(id, user.id);
	}

}