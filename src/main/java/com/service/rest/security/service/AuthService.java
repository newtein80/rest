package com.service.rest.security.service;

import com.service.rest.common.exception.exceptions.RestUnauthorizedException;
import com.service.rest.security.dto.RequestSignUpUser;
import com.service.rest.security.dto.RequestSocialData;
import com.service.rest.security.exception.UserNotFoundException;
import com.service.rest.security.model.Member;
import com.service.rest.security.model.enums.UserRole;

import javassist.NotFoundException;

public interface AuthService {

    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";

    //#region - auth by spring security
    void signUpUser(RequestSignUpUser requestSignUpUser);

    Member loginUser(String id, String password) throws UserNotFoundException, RestUnauthorizedException;
    //#endregion

    void signUpSocialUser(RequestSocialData member);

    Member loginSocialUser(String id, String type) throws NotFoundException;

    Member findByUsername(String username) throws NotFoundException;

    void verifyEmail(String key) throws NotFoundException;

    String sendVerificationMail(Member member) throws NotFoundException;

    void modifyUserRole(Member member, UserRole userRole);

    boolean isPasswordUuidValidate(String key);

    void changePassword(Member member, String password) throws NotFoundException;

    String requestChangePassword(Member member) throws NotFoundException;
}
