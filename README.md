# Artinus 과제 수행 (백엔드 지원자 김대현)

| 사용 기술       | 버전     |
|-------------|--------|
| Spring Boot | 3.4.3  |
| Java        | 17     |
| Mysql       | 8.0.41 |
| QueryDsl    | 5.0.0  |

| IDE      |
|----------|
| IntelliJ |

## Demo URL

| url                   | https://frankit.tonglink.site         |
|-----------------------|------------|


# application-dev.yml 수정
```yml
## 해당부분 실제 DB접속 주소로 설정 후 빌드
spring:  
  datasource:
    url: ${DB_URL:jdbc:mysql://host/database}
    username: ${DB_USERNAME:username}
    password: ${DB_PASSWORD:password}
```

# Docker Build
```shell
./gradlew bootJar
docker build --platform linux/amd64 -t altinustask:darren-0.0.1 .
docker run -d -p 8086:8080 altinustask:darren-0.0.1
```

# Jar Build
```shell
./gradlew bootJar
java -jar build/libs/Artinus-darren-0.0.1.jar
```


# 패키지 구조 (Layered Architecture)
```text
com.altinus
│── common                  # 공통 기능 (예외 처리, 인터셉터, 응답 처리 등)
│   ├── exception
│   │   ├── ... 
│   ├── interceptor
│   │   ├── ...
│   ├── response
│   │   ├── ...
│── subscription            # 구독 도메인
│   ├── application         # 비즈니스 로직 (Use Case)
│   │   ├── dto
│   │   │   ├── request
│   │   │   │   ├── SubscriptionCreateRequestDto.java   # 서비스 Layer에서 파라미터로 받는 DTO
│   │   │   ├── response
│   │   │   │   ├── SubscriptionResponseDto.java        # 서비스 Layer에서 결과값 반환 DTO
│   │   ├── converter
│   │   │   ├── SubscriptionConverter.java              # 각 Layer별 DTO 변환용 컨버터 클래스
│   │   ├── service
│   │   │   ├── SubscriptionService.java                # 인터페이스   
│   │   │   ├── AbstractSubscriptionService.java        # 추상 클래스
│   │   │   ├── impl
│   │   │   │   ├── BasicSubscriptionService.java       # 서비스 구현체
│   │   │   │   ├── PremiumSubscriptionService.java     # 서비스 구현체
│   │   ├── SubscriptionFacade.java                     # 퍼사드 패턴
│   ├── domain                                          # 도메인 모델
│   │   ├── common                                      # 공통 사용
│   │   │   ├── SubscriptionStatus.java
│   │   │   ├── SubscriptionDSLRepository.java
│   │   ├── subscription                                # 구독 도메인
│   │   │   ├── Subscription.java
│   │   │   ├── SubscriptionRepository.java
│   │   ├── channel                                     # 채널 도메인
│   │   │   ├── Channel.java
│   │   │   ├── ChannelRepository.java
│   │   ├── history                                     # 구독 이력 도메인
│   │   │   ├── SubscriptionHistory.java
│   │   │   ├── SubscriptionHistoryRepository.java
│   ├── infrastructure                                  # 영속성, QueryDSL, 외부 API 연동
│   │   ├── external                                    # 외부 API
│   │   │   ├── dto
│   │   │   │   ├── CsrngResponseDto.java
│   │   │   ├── service
│   │   │   │   ├── CsrngExternalService.java 
│   │   ├── repository
│   │   │   ├── SubscriptionDSLRepositoryImpl.java      # QueryDSL 구현체
│   ├── ui                  # API 레이어
│   │   ├── dto 
│   │   │   ├── request
│   │   │   │   ├── SubscriptionCreateRequestApiDto.java #View Layer에서 사용할 DTO
│   │   │   ├── response
│   │   │   │   ├── SubscriptionCreateResponseApiDto.java #View Layer에서 사용할 DTO
│   │   ├── SubscriptionController.java
│   │   ├── ChannelController.java
│── view            # Demo를 위한 ThymeLeaf 디렉토리
│   ├── ViewController.java
│── AltinusApplication.java  # 메인 클래스
```