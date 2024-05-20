<p align="center"><img src='https://github.com/CookShare-addinedu/CookShare/assets/83636742/e4568fd0-7492-4520-9492-4ab2a8acd2e7' width='40%'></p>

# 🍽️ 쿡쉐어
Cook-Share는 1인 가구의 고충을 해결하기 위해 시작된 ${\textsf{\color{#ff9000}음식 공유 플랫폼}}$입니다. 사용자들이 음식을 나눔으로써 비용을 절감하고, 환경을 보호하며, 
동네 친구를 만날 수 있는 장을 제공합니다. 이 플랫폼은 배달 음식의 비율을 줄이고, 친환경 목적의 웹 앱으로 설계되었습니다. 
사용자들은 게시판과 채팅 기능을 통해 소통하고 교류할 수 있습니다.
## Key Features

- **게시판**: 사용자는 글을 작성, 수정, 삭제할 수 있으며, 특정 키워드로 글을 검색하고, 마음에 드는 글이나 음식을 찜할 수 있습니다.
- **채팅**: 사용자 간 실시간으로 메시지를 주고받을 수 있는 기능을 제공합니다.
- **알림**: 중요한 정보나 메시지를 사용자에게 알립니다.
- **마이페이지**: 사용자는 자신의 개인 정보를 관리할 수 있습니다.

## Getting Started
### 설치 및 실행 단계
> client(프론트)와 server(백엔드) 둘다 설치, 실행해주세요 
### 프론트엔드 설치
1. **프로젝트 클론하기**
   
   Git을 사용하여 이 프로젝트를 로컬 시스템으로 클론합니다.
   ```bash
   https://github.com/CookShare-addinedu/CookShare.git
   cd client
   ```
3. **필요한 종속성을 설치**
   ```bash
    npm install
    ```
4. **애플리케이션을 실행**
    ```bash
    npm start
    ```
5. **환경설정**

   [ https://developers.kakao.com/](https://react-kakao-maps-sdk.jaeseokim.dev/docs/intro/) 에 접속하여 API키를 발급받으세요.

   client 루트 경로에 `.env` 파일을 생성한뒤 다음과 같이 입력합니다.
   ```
   #REACT_APP_KAKAO_KEY = 발급받은 API key를 넣어주세요 
   ```
### 백엔드 설치   
1. **프로젝트 클론하기**(프론트엔드와 동일한 레포지토리 사용)
   ```bash
   https://github.com/CookShare-addinedu/CookShare.git
   cd server
   ```
2. **필요한 종속성을 설치**
    ```bash
    ./gradlew build
    ```

3. **애플리케이션을 실행**
    ```bash
    ./gradlew bootRun
    ```
## Technology Stack
![image](https://github.com/CookShare-addinedu/CookShare/assets/99055686/568db4c7-77b1-45f1-a78e-e0b018ed6e79)


## Service Architecture
![아키텍처 다이어그램 drawio (1)](https://github.com/CookShare-addinedu/CookShare/assets/99055686/a3259655-c171-4ae1-a403-9b3a9acd8e85)

<details>
<summary><h2>Project Structure</h2></summary>
<div markdown="1">

## 백엔드 디렉토리 구조
```plaintext
server
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── foodshare
│   │   │           ├── board
│   │   │           │   ├── controller
│   │   │           │   ├── dto
│   │   │           │   ├── exception
│   │   │           │   ├── mapper
│   │   │           │   ├── repository
│   │   │           │   └── service
│   │   │           ├── chat
│   │   │           │   ├── annotation
│   │   │           │   ├── aspect
│   │   │           │   ├── controller
│   │   │           │   ├── dto
│   │   │           │   ├── exception
│   │   │           │   ├── mapper
│   │   │           │   ├── repository
│   │   │           │   ├── service
│   │   │           │   └── utils
│   │   │           ├── config
│   │   │           │   ├── WebSecurityConfig
│   │   │           │   └── WebSocketConfig
│   │   │           ├── domain
│   │   │           │   ├── Category
│   │   │           │   ├── ChatMessage
│   │   │           │   ├── ChatRoom
│   │   │           │   ├── FavoriteFood
│   │   │           │   ├── Food
│   │   │           │   ├── FoodImage
│   │   │           │   ├── Notification
│   │   │           │   ├── Spoon
│   │   │           │   ├── TradeReview
│   │   │           │   ├── User
│   │   │           │   └── UserChatRoomVisibility
│   │   │           ├── notification
│   │   │           │   ├── controller
│   │   │           │   ├── dto
│   │   │           │   ├── notificationutils
│   │   │           │   ├── repository
│   │   │           │   ├── service
│   │   │           │   └── sse
│   │   │           ├── security
│   │   │           │   ├── controller
│   │   │           │   ├── dto
│   │   │           │   ├── jwt
│   │   │           │   ├── repository
│   │   │           │   └── service
│   │   │           └── FoodShareApplication.java
│   │   └── resources
│   │       └── application.properties
└── gradle
    └── wrapper

```
### 프론트엔드 디렉토리 구조
```plaintext
client
│
├── node_modules
│
├── public
│   ├── img
│   ├── favicon.ico
│   └── index.html
│
├── src
│   ├── components
│   │   ├── address
│   │   ├── badge
│   │   ├── button
│   │   ├── card
│   │   ├── caution
│   │   ├── drawer
│   │   ├── list
│   │   ├── menu
│   │   └── select
│   │
│   ├── data
│   │   └── CautionData.js
│   │
│   ├── hook
│   │   ├── useDebounce.js
│   │   └── useHeaderTitle.js
│   │
│   ├── redux
│   │   ├── addressSlice.js
│   │   ├── foodSlice.js
│   │   └── store.js
│   │
│   ├── style
│   │   ├── Global.scss
│   │   └── style.scss
│   │
│   ├── views
│   │   ├── auth
│   │   ├── Chat
│   │   ├── common
│   │   ├── Notification
│   │   └── pages
│   │
│   ├── App.js
│   ├── index.js
│   └── router.js
│
├── .env
├── .gitignore
├── config-overrides.js
├── package.json
├── package-lock.json
└── README.md

```

</div>
</details>



## Links
**Wiki**: [Cook-Share Wiki](https://github.com/CookShare-addinedu/CookShare/wiki)

## Roles
<img width="100%" alt="image" src="https://github.com/CookShare-addinedu/CookShare/assets/83636742/c54c1927-ab78-4d0b-9fb5-537f5c8e5f55">


## License
MIT License Copyright (c) 2024 eun-su-jeong
