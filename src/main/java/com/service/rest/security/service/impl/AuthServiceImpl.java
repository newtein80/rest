package com.service.rest.security.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.service.rest.common.exception.exceptions.RestNotFoundException;
import com.service.rest.common.exception.exceptions.RestUnauthorizedException;
import com.service.rest.security.dto.RequestSignUpUser;
import com.service.rest.security.dto.RequestSocialData;
import com.service.rest.security.exception.TokenRefreshException;
import com.service.rest.security.exception.UserNotFoundException;
import com.service.rest.security.model.Member;
import com.service.rest.security.model.RefreshToken;
import com.service.rest.security.model.Role;
import com.service.rest.security.model.Salt;
import com.service.rest.security.model.SocialData;
import com.service.rest.security.model.enums.UserRole;
import com.service.rest.security.repository.MemberRepository;
import com.service.rest.security.repository.RefreshTokenRepository;
import com.service.rest.security.repository.RoleRepository;
import com.service.rest.security.repository.SocialDataRepository;
import com.service.rest.security.service.AuthService;
import com.service.rest.security.service.RefreshTokenService;
import com.service.rest.security.util.SaltUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private SocialDataRepository socialDataRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    public void signUpUser(RequestSignUpUser requestSignUpUser) {
        /**
         * Flow of user sign-up verification mail
         * 
         * 1. 사용자가 회원가입을한다. (회원가입을 한 User의 권한은 NOT_PERMITTED일 것이다.)
         * 2. 회원가입 완료와 동시에 사용자에게 회원인증 메일을 보낸다.
         * 3. 회원인증 메일에는 회원인증을 할 수 있는 링크를 보낸다.
         * 4. 사용자는 메일에 제시된 링크를 클릭했을시, 사용자의 role을 not permitted에서 user로 변경해준다.
         * 
         * 회원인증이 되지 않고 서버에 대한 서비스를 요구했을시, 클라이언트 측은 회원인증을 요구하는 페이지로 이동을 하고 서버는 접근 금지
         */

         // todo: 기존에 등록된 사용자 체크
         Member member = new Member(requestSignUpUser.getUsername(), requestSignUpUser.getPassword(), requestSignUpUser.getName(), requestSignUpUser.getEmail(), requestSignUpUser.getAddress());

         // ! 테스트: 강제로 Exception을 발생
         // List<String> testErrorOccur = null;
         // for (int i = 0; i < testErrorOccur.size(); i++) {
         //     System.out.println("tt");
         // }
         
         Set<Role> defaultRoles = new HashSet<>();
         // todo: 사용자가 새로 등록 될 때 무조건 기본 role을 정하는 방식으로 변경해도 됨. length == 0 일 경우 default role을 부여
         // ! 최초 가입 시 ROLE_NOT_PERMITTED 권한 발급
         // Set<String> strDefaultRoles = new HashSet<>();
         // strDefaultRoles = requestSignUpUser.getRole();
         // strDefaultRoles.forEach(role -> {
         //     switch (role) {
         //         case "USER":
         //             Role user_role = this.roleRepository.findByName(UserRole.ROLE_USER).orElseThrow(() -> new RuntimeException("tte"));
         //             defaultRoles.add(user_role);
         //             break;
             
         //         default:
         //             Role manage_role = this.roleRepository.findByName(UserRole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("tte"));
         //             defaultRoles.add(manage_role);
         //             break;
         //     }
         // });
 
         Role user_role = this.roleRepository.findByName(UserRole.ROLE_NOT_PERMITTED).orElseThrow(() -> new RestNotFoundException("The user's role could not be found."));
         defaultRoles.add(user_role);
 
         member.setRoles(defaultRoles);
 
         String password = member.getPassword();
         String salt = saltUtil.genSalt();
         member.setSalt(new Salt(salt));
         member.setPassword(saltUtil.encodePassword(salt, password));
         memberRepository.save(member);
        
    }

    @Override
    public Member loginUser(String id, String password) throws UserNotFoundException, RestUnauthorizedException {
        // ! 테스트: 강제로 Exception을 발생
        // List<String> testErrorOccur = null;
        // for (int i = 0; i < testErrorOccur.size(); i++) {
        //     System.out.println("tt");
        // }

        Member member = this.memberRepository.findByUsername(id).orElseThrow(() -> new UserNotFoundException("Member not existed"));
        
        String salt = member.getSalt().getSalt();
        password = saltUtil.encodePassword(salt, password);

        if (!member.getPassword().equals(password))
            throw new RestUnauthorizedException("The password is wrong.");
        // if (member.getSocial() != null)
        //     throw new RestUnauthorizedException("Please log in with your social account.");

        return member;
    }

    @Override
    @Transactional
    public void signUpSocialUser(RequestSocialData member) {
        Member newMember = new Member();
        newMember.setUsername(member.getId());
        newMember.setPassword("");
        newMember.setEmail(member.getEmail());
        newMember.setName(member.getName());
        newMember.setAddress("");
        newMember.setSocial(new SocialData(member.getId(), member.getEmail(), member.getType()));
        memberRepository.save(newMember);
    }

    @Override
    public Member loginSocialUser(String id, String type) throws NotFoundException {
        SocialData socialData = socialDataRepository.findByIdAndType(id, type);
        if (socialData == null)
            throw new RestNotFoundException("Member not existed");
        return socialData.getMember();
    }

    @Override
    public Member findByUsername(String username) throws NotFoundException {
        Member member = this.memberRepository.findByUsername(username).orElseThrow(() -> new RestNotFoundException("Member not existed"));
        return member;
    }

    @Override
    @Transactional
    public void verifyEmail(String key) throws RestNotFoundException, TokenRefreshException {
        /**
         * 요청된 키값을 받았을 시 redis에 key값이 있는지 유효성을 확인한 뒤 있을 경우, Key값에 존재하는 Value값의 유저의 권한을 변경하도록 하는 코드
         */
        // ! redis -> database
        // String memberId = redisUtil.getData(key);
        RefreshToken refreshToken = this.refreshTokenRepository.findByToken(key).orElseThrow(() -> new TokenRefreshException("Refresh token[" + key + "] was expired. Please make a new signin request")); // throws 와 같은 type의 exception을 throw해야함
        refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        Member member = memberRepository.findById(refreshToken.getMember().getId()).orElseThrow(() -> new RestNotFoundException("Member not existed")); // throws 와 같은 type의 exception을 throw해야함

        modifyUserRole(member, UserRole.ROLE_USER);
        // ! redis -> database
        // redisUtil.deleteData(key);
        this.refreshTokenRepository.deleteByToken(key);
    }

    @Override
    public String sendVerificationMail(Member member) throws NotFoundException {
        /**
         * 회원가입시 동봉된 링크
         * 링크의 경우, 그 url값을 고정시키지 않고 유동적으로 변경함으로써, 제시된 경로가 아니면 함부로 회원의 권한을 변경할 수 없도록 해야 한다.
         * redis를 사용할 것이다. 회원가입 인증 메일을 '요청'한 사용자에 한해 몇 분 동안만 그 링크를 살아있게 함으로써 보안적인 측면을 강화.
         * 
         * redis를에 들어가는 데이터 값
         * ---------------------------------------------
         * key                     | value
         * ---------------------------------------------
         * uuid(or RandomString)   | userId
         * ---------------------------------------------
         */
        String VERIFICATION_LINK = "http://localhost:8282/user/verify/";
        if (member == null)
            throw new RestNotFoundException("Member not existed");
        UUID uuid = UUID.randomUUID();

        refreshTokenService.saveByRefreshToken(
            // todo: REFRESH_TOKEN_VALIDATION_SECOND 등의 값을 가져올 수 있는 properties class를 만들어 사용하는 방식으로 변경
            RefreshToken.builder().member(member).token(uuid.toString()).expiredDate(new Date((new Date()).getTime() + 60 * 60 * 30L)).build()
        );

        // ! redis -> database
        // redisUtil.setDataExpire(uuid.toString(), member.getUsername(), 60 * 30L);
        // ! 메일보내기 주석처리
        // emailService.sendMail(member.getEmail(), "[김동근 스프링] 회원가입 인증메일입니다.", VERIFICATION_LINK + uuid.toString()); 
        return "To: " + member.getEmail() + " , Message : [" + member.getName() + "] This is the membership registration confirmation email., Link : " + VERIFICATION_LINK + uuid.toString(); // ! 메일보내기 주석처리
    }

    @Override
    public void modifyUserRole(Member member, UserRole userRole) {
        Set<Role> defaultRoles = new HashSet<>();
        Role user_role = this.roleRepository.findByName(userRole).orElseThrow(() -> new RestNotFoundException("[" + userRole.name() + "] do not exist."));
        defaultRoles.add(user_role);

        member.setRoles(defaultRoles);
        memberRepository.save(member);
        
    }

    @Override
    public boolean isPasswordUuidValidate(String key) {
        // ! redis -> database
        // String memberId = redisUtil.getData(key);
        String userName = this.refreshTokenRepository.findByToken(key).orElseThrow(
            () -> new RestNotFoundException("Verification key[" + key + "] was expired. Resend the password change request.")
        ).getMember().getUsername();

        return !userName.equals("");
    }

    @Override
    public void changePassword(Member member, String password) throws NotFoundException {
        if (member == null)
            throw new RestNotFoundException("Member not existed.");

        String salt = saltUtil.genSalt();
        member.setSalt(new Salt(salt));
        member.setPassword(saltUtil.encodePassword(salt, password));
        memberRepository.save(member);

        this.refreshTokenRepository.deleteByMember(member);
        
    }

    @Override
    public String requestChangePassword(Member member) throws NotFoundException {
        /**
         * 비밀번호 찾기도 회원가입 인증 메일과 같은 플로우로 진행
         * 
         * 1. 사용자의 id와 이메일 정보가 맞으면 메일을 보낸다.
         * 2. 메일에는 비밀번호를 변경할 수 있는 페이지의 주소가 담겨있다. (물론 메일 링크는 지정된 시간내에만 살아있어야 한다.)
         * 3. 지정된 페이지로부터 사용자의 비밀번호 변경요청을 받는다.
         * 4. 비밀번호를 변경한다.
         */
        String CHANGE_PASSWORD_LINK = "http://localhost:8282/user/password/";
        if (member == null)
            throw new RestNotFoundException("Member not existed.");
        String key = REDIS_CHANGE_PASSWORD_PREFIX + UUID.randomUUID();

        // ! redis -> database
        // redisUtil.setDataExpire(key, member.getUsername(), 60 * 30L);
        refreshTokenService.saveByRefreshToken(
            RefreshToken.builder().member(member).token(key).expiredDate(new Date((new Date()).getTime() + 60 * 30L)).build()
        );
        // ! 메일 발송 주석 처리
        // emailService.sendMail(member.getEmail(), "[김동근 스프링] 사용자 비밀번호 안내 메일", CHANGE_PASSWORD_LINK + key);
        return "To: " + member.getEmail() + " , Message : [" + member.getName() + "] This is the user confirmation email to change the password., Link : " + CHANGE_PASSWORD_LINK + key; // ! 메일보내기 주석처리
    }
    
}
