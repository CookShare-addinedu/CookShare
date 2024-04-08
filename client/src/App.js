import React from 'react';
import './App.css';
import {Link, Route, Routes} from "react-router-dom";
import FoodForm from "./views/Board/FoodForm";

function App() {
    return (
        <div className="app">
            <Header />
            <MessageList messages={dummyMessages} />
            <NavigationBar />
            <Routes>
                <Route path="/add" element={<FoodForm/>} />
                {/*<Route path="/search" element={<Search/>} />*/}
                {/*<Route path="/country/:code" element={<Country/>} />*/}
                {/*<Route path="*" element={<NotFound/>} />*/}
            </Routes>
            <div>
                <Link to={"/add"}>AddFood</Link>
                {/*<Link to={"/search"}>Search</Link>*/}
                {/*<Link to={"/country"}>Country</Link>*/}
            </div>
        </div>
    );
}

function Header() {
    return (
        <div className="header">
            <button className="menu-button">☰</button>
            <div className="search-box">
                <input type="text" placeholder="Search" />
            </div>
            <div className="icons">
                <span>🔍</span>
                <span>👤</span>
                <span>5</span>
            </div>
        </div>
    );
}

function MessageList({ messages }) {
    return (
        <div className="message-list">
            {messages.map((message, index) => (
                <MessageCard key={index} message={message} />
            ))}
        </div>
    );
}

function MessageCard({ message }) {
    return (
        <div className="message-card">
            <div className="message-content">
                <h3>{message.title}</h3>
                <p>{message.body}</p>
            </div>
        </div>
    );
}

function NavigationBar() {
    return (
        <div className="navigation-bar">
            <span>Home</span>
            <span>Chatting</span>
            <span>Orders</span>
        </div>
    );
}

const dummyMessages = [
    {
        title: "제목이에요",
        body: "내용을 작성해주세요."
    },
    // ... 기타 메시지 객체
];

export default App;
