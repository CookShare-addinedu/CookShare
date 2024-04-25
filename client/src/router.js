import Login from "./views/auth/login/Login";
import {Header1, Header2, Header3, Header4, Header5} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";

import Chat from "./views/Chat/Chat";
import ChatRoomList from "./views/Chat/ChatRoomList";


import OnBoarding from "./views/pages/onboarding/OnBoarding";

import Register from "./views/auth/register/Register";
import Mypage from "./views/pages/MyPage/Mypage";

const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            title: null,
            header:<Header2/>,
            footer:null
        },
        {
            path: '/register',
            component: <Register/>,
            title: '회원가입',
            header:<Header2/>,
            footer:null,
        },
        {
            path: '/chat/GetChatList',
            component: <ChatRoomList/>,
            header: <Header5/>,
            footer: <Footer/>
        },
        {
            path: '/chat/GetChat/:chatRoomId',
            component: <Chat/>,
            header: <Header5/>,
            footer: <Footer/>
        },
        {
            path: '/',
            component: <OnBoarding/>,
            header: null,
            footer: null
        },
        {
            path: '/mypage',
            component: <Mypage/>,
            title: '나의 냉장고',
            header: <Header2/>,
            footer:<Footer/>
        }


    ]
}
export default router;