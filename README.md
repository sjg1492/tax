## 소개
본 프로젝트는 사용자가 회원가입과 로그인을 통해 서비스를 이용할 수 있도록 하며, 
외부 사이트에서 회원정보를 스크래핑하여 환급금을 계산하는 기능을 제공합니다. 

## 기능 목록
- **회원가입**: 사용자는 회원가입을 통해 서비스에 가입할 수 있습니다.
- **로그인**: 사용자는 로그인하여 JWT 인증 토큰을 발급받을 수 있습니다.
- **내 정보 보기**: 로그인한 사용자는 자신의 정보를 확인할 수 있습니다.
- **회원정보 스크래핑**: 외부 사이트에서 회원정보를 스크래핑하여 저장합니다.
- **환급금 계산**: 스크래핑한 데이터를 바탕으로 환급금을 계산합니다.

## 기술 스택
- Java 17
- Spring Boot 3.3
- JPA
- WebClient
- H2 Database (Memory Mode)
- Gradle
