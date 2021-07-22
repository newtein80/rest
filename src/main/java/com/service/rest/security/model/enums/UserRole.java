package com.service.rest.security.model.enums;

/**
 * 사용자 권한 관련 클래스 - DB 관리
 */
public enum UserRole {
    ROLE_NOT_PERMITTED,
    ROLE_USER,
    ROLE_MANAGER,
    ROLE_ADMIN,
    ROLE_MONITOR,
    ROLE_BASIC
}
