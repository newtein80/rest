#### TOY PROJECT

##### code
- postgresql v11
##### 1
- flyway 설정 (데이터베이스 형상관리)
  - src\main\resources\db\migration 폴더에 sql 파일이 없으면 에러나기 때문에 기본적인 것만 넣어놓음
- pom.xml (maven 관리)
  - 지금 있는거에서 각각의 용도는 알아볼 것
- .gitignore (git 무시 파일 설정)
  - https://www.toptal.com/developers/gitignore 환경에 맞게 세팅
- application.properties (데이터베이스 연결 정보 etc)
  - 데이터베이스 문자열 및 기타 설정 관련 값들 --> 각각 링크를 들어가 설명 볼 것

##### create database

```sql
-- Database: restsvc

-- DROP DATABASE restsvc;

CREATE DATABASE restsvc
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
```

##### 2
- spring security 관련 class 생성
  - Exception handling 관련 클래스 설정 - common에 custom exception class 생성

##### 3
- spring security 관련 설정

##### 4
- spring 인증관련 설정

##### 5
- 사용자 로그인 등등 코드 추가



