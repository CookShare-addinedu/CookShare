# FoodShare
에드인에듀 파이널 프로젝트 


Front directory tree (은수정)

📦client  
 ┣ 📂node_modules  
 ┃  
 ┣ 📂public  
 ┃  
 ┣ 📂src  
 ┃ ┣ 📂assets  
 ┃ ┃ ┗ 📂img  
 ┃ ┃  
 ┃ ┣ 📂components  
 ┃ ┃ ┣ 📂style  
 ┃ ┃ ┗ 📂common  
 ┃ ┃   ┣ 📜Header.jsx  
 ┃ ┃   ┗ 📜Footer.jsx  
 ┃ ┃  
 ┃ ┣ 📂views  
 ┃ ┃ ┣ 📂auth  
 ┃ ┃ ┃ ┣ 📜Login.jsx         # 로그인 화면  
 ┃ ┃ ┃ ┗ 📜SignUp.jsx        # 회원가입 화면  
 ┃ ┃ ┃  
 ┃ ┃ ┗ 📂pages  
 ┃ ┃   ┣ 📂Splash  
 ┃ ┃   ┃ ┣ 📜Splash.jsx        # 스플래쉬 화면  
 ┃ ┃   ┃ ┗ 📜Splash.scss       # 스플래쉬 화면 스타일  
 ┃ ┃   ┃  
 ┃ ┃   ┣ 📂Onboarding  
 ┃ ┃   ┃ ┣ 📜Onboarding.jsx    # 온보딩 화면  
 ┃ ┃   ┃ ┗ 📜Onboarding.scss   # 온보딩 화면 스타일  
 ┃ ┃   ┃  
 ┃ ┃   ┣ 📂Board  
 ┃ ┃   ┃ ┣ 📜Board.jsx         # 게시판 목록 화면  
 ┃ ┃   ┃ ┗ 📜Board.scss        # 게시판 목록 화면 스타일  
 ┃ ┃   ┃  
 ┃ ┃   ┣ 📂BoardDetail  
 ┃ ┃   ┃ ┣ 📜BoardDetail.jsx   # 게시판 상세페이지  
 ┃ ┃   ┃ ┗ 📜BoardDetail.scss  # 게시판 상세페이지 스타일  
 ┃ ┃   ┃  
 ┃ ┃   ┣ 📂Chat  
 ┃ ┃   ┃ ┣ 📜Chat.jsx          # 채팅 화면  
 ┃ ┃   ┃ ┗ 📜Chat.scss         # 채팅 화면 스타일  
 ┃ ┃   ┃  
 ┃ ┃   ┗ 📂MyPage  
 ┃ ┃     ┣ 📜MyPage.jsx        # 마이페이지  
 ┃ ┃     ┗ 📜MyPage.scss       # 마이페이지 스타일  
 ┃ ┃  
 ┃ ┣ 📜App.js  
 ┃ ┗ 📜index.js  
 ┃  
 ┣ 📜.gitignore  
 ┣ 📜package.json  
 ┣ 📜package-lock.json  
 ┗ 📜README.md  


Backend directory tree (김강민)

📦com.foodshare  
 ┣ 📂config  
 ┃ ┣ 📜RestTemplateConfig.java  
 ┃ ┣ 📜SecurityConfig.java  
 ┃ ┗ 📜UserPasswordEncoder.java  
 ┣ 📂controller  
 ┃ ┣ 📜HomeController.java  
 ┃ ┣ 📜UserController.java  
 ┃ ┗ 📜UserApiController.java  
 ┣ 📂domain  
 ┃ ┣ 📜Alarm.java  
 ┃ ┣ 📜Category.java  
 ┃ ┣ 📜FavoriteFood.java  
 ┃ ┣ 📜Food.java  
 ┃ ┣ 📜FoodImage.java  
 ┃ ┣ 📜Notification.java  
 ┃ ┣ 📜Plan.java  
 ┃ ┣ 📜SnsInfo.java  
 ┃ ┣ 📜Spoon.java  
 ┃ ┣ 📜TradeReview.java  
 ┃ ┗ 📜User.java  
 ┣ 📂dto  
 ┃ ┣ 📜EventDto.java  
 ┃ ┗ 📜LoginDto.java  
 ┣ 📂mobilenumberverify  
 ┃ ┣ 📜MobileNumberConfig.java  
 ┃ ┣ 📜MobileNumberService.java  
 ┃ ┗ 📜MobileNumberVerificationController.java  
 ┣ 📂oauth2  
 ┃ ┣ 📂exception  
 ┃ ┣ 📂handler  
 ┃ ┣ 📂service  
 ┃ ┣ 📂user  
 ┃ ┣ 📂util  
 ┃ ┗ 📜HttpCookieOauth2AuthorizationRequestRepository.java  
 ┣ 📂repository  
 ┃ ┣ 📜SnsInfoRepository.java  
 ┃ ┣ 📜UserLogRepository.java  
 ┃ ┗ 📜UserRepository.java  
 ┣ 📂service  
 ┃ ┗ 📜UserService.java  
 ┣ 📂util  
 ┃ ┗ 📜BooleanToNumberConverter.java  
 ┗ 📜FoodShareApplication.java  

 
