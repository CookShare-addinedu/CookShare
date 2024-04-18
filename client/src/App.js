import React from 'react';
import './App.css';
import {Link, Route, Routes} from "react-router-dom";
import FoodForm from "./views/Board/FoodForm";
import FoodList from "./views/Board/FoodList";
import FoodDetail from "./views/Board/FoodDetail";

function App() {
    return (
        <div className="app">
            <Header />
            <NavigationBar />
            <Routes>
                <Route path="/" element={<FoodList />} />
                <Route path="/add" element={<FoodForm />} />
                <Route path="/foods/:id" element={<FoodDetail />} />
                <Route path="/edit-food/:id" element={<FoodForm />} />
            </Routes>
            <div>
                <Link to="/">Home</Link><br></br>
                <Link to="/add">Add Food</Link>
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



export default App;
