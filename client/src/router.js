import Login from "./views/auth/login/Login";
import {Header1, Header2, Header3, Header4, Header5} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";
import Chat from "./views/Chat/Chat";
import ChatRoomList from "./views/Chat/ChatRoomList";

const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            header: <Header2/>,
            footer: null
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
        }


    ]
}
export default router;