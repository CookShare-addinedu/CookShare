<p align="center"><img src='https://github.com/CookShare-addinedu/CookShare/assets/83636742/e4568fd0-7492-4520-9492-4ab2a8acd2e7' width='40%'></p>

# 🍽️ 쿡쉐어
Cook-Share는 1인 가구의 고충을 해결하기 위해 시작된 ${\textsf{\color{#ff9000}음식 공유 플랫폼}}$ **음식 공유 플랫폼** **<span style="color:#ff9000">음식 공유 플랫폼</span>** 입니다. 사용자들이 음식을 나눔으로써 비용을 절감하고, 환경을 보호하며, 
동네 친구를 만날 수 있는 장을 제공합니다. 이 플랫폼은 배달 음식의 비율을 줄이고, 친환경 목적의 웹 앱으로 설계되었습니다. 
사용자들은 게시판과 채팅 기능을 통해 소통하고 교류할 수 있습니다.
## Key Features

- **게시판**: 사용자는 글을 작성, 수정, 삭제할 수 있으며, 특정 키워드로 글을 검색하고, 마음에 드는 글이나 음식을 찜할 수 있습니다.
- **채팅**: 사용자 간 실시간으로 메시지를 주고받을 수 있는 기능을 제공합니다.
- **알림**: 중요한 정보나 메시지를 사용자에게 알립니다.
- **마이페이지**: 사용자는 자신의 개인 정보를 관리할 수 있습니다.

## System Requirements

### Backend Configuration - Gradle Setup

#### Plugins
- `java`
- `org.springframework.boot` version `3.2.4`
- `io.spring.dependency-management` version `1.1.4`

#### Java Settings
Java 17을 사용합니다.
```groovy
java {
    sourceCompatibility = '17'
}
```

#### Dependencies
- **Spring Boot**: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-data-mongodb`, `spring-boot-starter-websocket`, `spring-boot-starter-security`
- **Database**: `mysql:mysql-connector-java:8.0.33`
- **Security**: `org.springframework.security:spring-security-test`
- **Logging**: `org.bgee.log4jdbc-log4j2:log4jdbc-log4j2-jdbc4.1:1.16`
- **JWT**: `io.jsonwebtoken:jjwt-api:0.11.5`, `jjwt-impl:0.11.5`, `jjwt-jackson:0.11.5`
- **Redis**: `org.springframework.boot:spring-boot-starter-data-redis`, `redis.clients:jedis`
- **Additional Utilities**: Lombok, MapStruct, OAuth2.

### Frontend Configuration - package.json

#### Main Libraries
- **React**: version `18.2.0`
- **Redux**: version `5.0.1`
- **Axios**: version `1.6.8`
- **Font Awesome**: version `6.5.2`
- **RSuite**: version `5.59.2`
- **React Router**: version `6.22.3`
- **Sass**: version `1.72.0`

#### Scripts
프로젝트의 시작, 빌드, 테스트 등을 위한 스크립트를 정의합니다.

### Getting Started
1. **프로젝트 클론하기**
   ```bash
   git clone https://example.com/cook-share.git
   ```
2. **프로젝트 디렉토리로 이동**
   ```bash
   cd cook-share
   ```
3. **환경 설정 파일 구성**
   ```bash
   # application.properties 파일을 열고 필요한 환경 설정을 구성
   ```
4. **Spring Boot 애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```
5. **React 애플리케이션 실행**
   ```bash
   npm start
   ```

## Source Architecture
```plaintext
com.cookshare
├── board
│   ├── controller
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── repository
│   └── service
├── chat
│   ├── annotation
│   ├── aspect
│   ├── controller
│   ├── dto
│   ├── exception
│   ├── mapper
│   ├── repository
│   ├── service
│   └── utils
├── config
│   ├── WebSecurityConfig
│   └── WebSocketConfig
├── domain
│   ├── Category
│   ├── ChatMessage
│   ├── ChatRoom
│   ├── FavoriteFood
│   ├── Food
│   ├── FoodImage
│   ├── Notification
│   ├── Spoon
│   ├── TradeReview
│   ├── User
│   └── UserChatRoomVisibility
├── notification
│   ├── controller
│   ├── dto
│   ├── notificationutils
│   ├── repository
│   ├── service
│   └── sse
├── security
│   ├── controller
│   ├── dto
│   ├── jwt
│   ├── repository
│   └── service
└── CookShareApplication.java
```

## Links
- **Wiki**: [Cook-Share Wiki](https://github.com/CookShare-addinedu/CookShare/wiki)

---
