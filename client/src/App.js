import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./views/Chat/Chat";
import React from "react";
import './App.css'
import Home from "./views/auth/Home";
import Login from "./views/auth/Login";
import Welcome from "./views/auth/Welcome";
import Register from "./views/auth/Register";
import ChatRoomList from "./views/Chat/ChatRoomList";

function App() {
    return (
            <div className="App">
                {/*<h1>푸드쉐어페이지입니다.</h1>*/}
                <Routes>
                    <Route path={"/"} element={<Home/>}/>
                    <Route path={"/Login"} element={<Login/>}/>
                    <Route path={"/Welcome"} element={<Welcome/>}/>
                    <Route path={"/Register"} element={<Register/>}/>
                    {/*<Route path={"/Profile"} element={<Profile />} />*/}


                    {/*<Route path="/chat/GetChatList" element={<ChatRoomList/>}/>*/}
                    {/*<Route path="/chat/GetChat" element={<Chat/>}/>*/}

                    {/* 채팅방 목록 페이지 */}
                    <Route path="/chat/GetChatList" element={<ChatRoomList />} />

                    {/* 개별 채팅 페이지 */}
                    <Route path="/chat/GetChat/:chatRoomId" element={<Chat />} />



                </Routes>
            </div>
        )

}

export default App;
