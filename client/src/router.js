import Login from "./views/auth/login/Login";
import Register from "./views/auth/register/Register";
import {Header1, Header2, Header3, Header4} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";
import Main from "./views/pages/main/Main";
import OnBoarding from "./views/pages/onboarding/OnBoarding";
import Mypage from "./views/pages/mypage/Mypage";
import Drawers from "./components/drawer/Drawers";
import Search from "./views/pages/search/Search";
import SearchAddress from "./views/pages/searchaddress/SearchAddress";
import MainDetail from "./views/pages/main/MainDetail";
import MainForm from "./views/pages/main/MainForm";
import ChatRoomList from "./views/Chat/ChatRoomList";
import Chat from "./views/Chat/Chat";
import ChatForm from "./views/Chat/ChatButton";
import Notifications from "./views/Notification/Notifications";

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
            path: '/main',
            component: <Main/>,
            title: null,
            header:<Header4/>,
            footer:<Footer/>
        },
        {
            path: '/main/foods/:id',
            component: <MainDetail/>,
            title: null,
            header:<Header3/>,
            footer:null
        },
        {
            path: '/main/add',
            component: <MainForm/>,
            title: null,
            header:<Header1/>,
            footer:null
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
            component: <Mypage/>,
            title: '나의 냉장고',
            header: <Header1/>,
            footer:<Footer/>
        },
        {
            path: '/drawer',
            component: <Drawers/>,
            title: '팝업테스트중이올시다',
            header: <Header1/>,
            footer: <Footer/>
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
            path: '/chat/getChatList',
            component: <ChatRoomList/>,
            header: <Header4/>,
            footer: <Footer/>
        },
        {
            path: '/chat/getChat/:chatRoomId',
            component: <Chat/>,
            header: <Header4/>,

        },
        {
            path: '/chat/chatForm',
            component: <ChatForm/>,
            header: <Header4/>,

        },

        {
            path: 'notification',
            component: <Notifications/>,
            header: <Header4/>,

        },

    ]
}
export default router;