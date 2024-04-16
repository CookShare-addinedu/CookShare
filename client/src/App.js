//import './style/style.scss';
import  './App.css';
import {useMediaQuery} from "react-responsive";
import {Route, Routes} from "react-router-dom";
import Login from "./views/auth/login/Login";
import Register from "./views/auth/register/Register";
import Mypage from "./views/auth/myPage/Mypage";
import Header from "./components/Header";
import Welcome from "./views/auth/welcome/Welcome";

import Chat from "./views/Chat/Chat";
import ChatRoomList from "./views/Chat/ChatRoomList";

//import Board from "./views/pages/board/Board";

function App() {
    return (
        <div className={"App"}>
            <header className={"App-header"}/>
                <Routes>
                    <Route path={"/"} element={<Header />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/welcome" element={<Welcome />} />
                    <Route path="/register" element={<Register />} />
                    {/*<Route path="/" element={<Board />} />*/}
                    <Route path="/mypage" element={<Mypage />} />

                    {/*<Route path="/chat/GetChatList" element={<ChatRoomList/>}/>*/}
                    {/*<Route path="/chat/GetChat" element={<Chat/>}/>*/}
                    {/* 채팅방 목록 페이지 */}
                    <Route path="/chat/GetChatList" element={<ChatRoomList />} />
                    {/* 개별 채팅 페이지 */}
                    <Route path="/chat/GetChat/:chatRoomId" element={<Chat />} />

                </Routes>
            </div>
    );
}

export default App;