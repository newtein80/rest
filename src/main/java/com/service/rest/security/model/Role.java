package com.service.rest.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id; // persistence == DB랑 관계가 있다.
import javax.persistence.Table;

import com.service.rest.security.model.enums.UserRole;

@Entity // ! DB에 자동으로 테이블이 생성됨 JPA가...
@Table(schema = "rest", name = "role")
public class Role {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private UserRole name;

	public Role() {

	}

	public Role(UserRole name) {
		this.name = name;
	}

    //#region- getter, setter
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserRole getName() {
		return name;
	}

	public void setName(UserRole name) {
		this.name = name;
	}
    //#endregion
}
