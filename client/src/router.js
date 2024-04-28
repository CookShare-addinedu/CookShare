import Login from "./views/auth/login/Login";
import {Header1, Header2, Header3, Header4, Header5} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";




import OnBoarding from "./views/pages/onboarding/OnBoarding";

import Register from "./views/auth/register/Register";
import Search from "./views/pages/search/Search";
import SearchAddress from "./views/pages/searchaddress/SearchAddress";
import MyPage from "./views/pages/MyPage/MyPage";

import Chat from "./views/Chat/Chat";
import ChatRoomList from "./views/Chat/ChatRoomList";


const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            title: null,
            header:<Header1/>,
            footer:null
        },
        {
            path: '/register',
            component: <Register/>,
            title: '회원가입',
            header:<Header1/>,
            footer:null,
        },
        {
            path: '/',
            component: <OnBoarding/>,
            title: null,
            header:null,
            footer:null
        },
        {
            path: '/mypage',
            component: <MyPage/>,
            title: '나의 냉장고',
            header: <Header1/>,
            footer:<Footer/>
        },
        {
            path: '/search',
            component: <Search/>,
            title: null,
            header: null,
            footer: null
        },
        {
            path: '/searchAddress',
            component: <SearchAddress/>,
            title: null,
            header: null,
            footer: null
        },
        {
            path:'/chat/getChatList',
            component: <ChatRoomList/>,
            header: <Header4/>,
            footer:<Footer/>
        },
        {
            path:'/chat/getChat/:chatRoomId',
            component: <Chat/>,
            header: <Header4/>,
            footer:<Footer/>
        },



    ]
}
export default router;