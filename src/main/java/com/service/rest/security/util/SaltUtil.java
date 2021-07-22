package com.service.rest.security.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

/**
 * https://velog.io/@ehdrms2034/Spring-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%95%94%ED%98%B8%ED%99%94%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B3%A0%EC%B0%B0
 * 
 * 해쉬함수는 단방향 암호화 방식
 * 복호화가 어렵다.
 * RSA에 비해 비용이 적게 든다.
 * 무결성을 체크
 * 하나의 평문에는 언제나 같은 해쉬 함수를 내뱉는다.
 *      해쉬함수의 약점
 *      모든 비밀번호에 대한 해쉬값을 테이블로 만들어서 무차별적으로 대입 공격
 *      이렇게 모든 평문에 대해 해쉬값을 기록한 것을 레인보우 테이블이라 하고, 레인보우 테이블을 이용하여 무차별적인 로그인 시도를 하는 것을 레인보우 테이블 어택이라한다.
 * 
 * Salt
 * 해쉬 함수를 만들기 전에 평문값에 SALT라는 랜덤 값을 추가로 넣어서 해쉬함수를 만들겠다는 것
 * 즉, 어떤 Salt를 사용했는지 공격하는 측에선 알 수 없기 때문에, 레인보우 테이블을 만드는데 오래 걸린다.
 * 이런 해쉬함수에 Salt를 치게 만드는 방법은 PDKDF2와 BCrypt가 존재
 */
@Service
public class SaltUtil {

    public String encodePassword(String salt, String password){
        return BCrypt.hashpw(password,salt);
    }

    public String genSalt(){
        return BCrypt.gensalt(); // ! **
    }

}