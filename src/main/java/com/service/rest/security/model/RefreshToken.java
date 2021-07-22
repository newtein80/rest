package com.service.rest.security.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // ! JPA에서 테이블로 간주
@Table(schema = "rest", name = "refreshtoken") // ! JPA에서 테이블 생성시 스키마, 이름 등등을 지정
@Getter
@NoArgsConstructor // ! 생성자 - parameter가 없는 생성자
@AllArgsConstructor // ! 생성자 - 멤버변수 전부 parameter로 받는 생성자를 자동으로 만들어줌
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // @Column(columnDefinition = "TIMESTAMP (3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredDate;
}

