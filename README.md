
# 패키지 구조
```text
com.altinus
│── common                  # 공통 기능 (예외 처리, 인터셉터, 응답 처리 등)
│   ├── exception
│   ├── interceptor
│   ├── response
│── subscription            # 구독 도메인
│   ├── application         # 비즈니스 로직 (Use Case)
│   │   ├── dto
│   │   │   ├── request
│   │   │   │   ├── SubscriptionCreateRequestDto.java
│   │   │   │   ├── SubscriptionUpdateRequestDto.java
│   │   │   ├── response
│   │   │   │   ├── SubscriptionResponseDto.java
│   │   │   │   ├── SubscriptionHistoryResponseDto.java
│   │   │   │   ├── ChannelListResponseDto.java
│   │   ├── converter
│   │   │   ├── SubscriptionConverter.java
│   │   ├── service
│   │   │   ├── SubscriptionService.java  # 인터페이스
│   │   │   ├── AbstractSubscriptionService.java  # 추상 클래스
│   │   │   ├── impl
│   │   │   │   ├── BasicSubscriptionService.java
│   │   │   │   ├── PremiumSubscriptionService.java
│   │   ├── SubscriptionFacade.java
│   ├── domain              # 도메인 모델
│   │   ├── subscription    # 구독 도메인
│   │   │   ├── Subscription.java
│   │   │   ├── SubscriptionRepository.java
│   │   ├── channel         # 채널 도메인
│   │   │   ├── Channel.java
│   │   │   ├── ChannelRepository.java
│   │   ├── history         # 구독 이력 도메인
│   │   │   ├── SubscriptionHistory.java
│   │   │   ├── SubscriptionHistoryRepository.java
│   ├── infrastructure      # 영속성, QueryDSL, 외부 API 연동
│   │   ├── repository
│   │   │   ├── SubscriptionQueryRepository.java
│   │   │   ├── SubscriptionQueryRepositoryImpl.java
│   │   │   ├── SubscriptionHistoryQueryRepository.java
│   │   │   ├── SubscriptionHistoryQueryRepositoryImpl.java
│   ├── ui                  # API 레이어
│   │   ├── SubscriptionController.java
│   │   ├── ChannelController.java
│   │   ├── SubscriptionHistoryController.java
│
│── AltinusApplication.java  # 메인 클래스
```