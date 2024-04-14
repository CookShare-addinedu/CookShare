import React, {useEffect, useRef, useState} from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.scss';
import {jwtDecode} from 'jwt-decode';
import useChatDetailList from "./useChatDetailList";
import {useParams} from "react-router-dom";

function Chat() {
    const {chatRoomId} = useParams(); // URL에서 roomId 추출 . app.js ChatRoomList
    const [stompClient, setStompClient] = useState(null);
    const [newMessage, setNewMessage] = useState("");
    const endOfMessagesRef = useRef(null);

    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const firstUser = decoded.uid;

    const {messageList, addMessageList} = useChatDetailList(chatRoomId, firstUser);

    useEffect(() => {
        if (stompClient) {
            return;
        }
        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, () => {
            console.log('웹소켓 연결');
            client.subscribe(`/topic/chat/room/${chatRoomId}`,
                (message) => {
                    const receivedMessage = JSON.parse(message.body);
                    addMessageList(receivedMessage);
                });

            setStompClient(client);
        }, (error) => {
            console.error('웹소켓 연결에러: ', error);
        });

    }, [chatRoomId, addMessageList]);

    // 스크롤을 마지막 메시지로 이동
    useEffect(() => {
        if (endOfMessagesRef.current) {
            endOfMessagesRef.current.scrollIntoView({behavior: 'smooth'});
        }
    }, [messageList]);

    // 컴포넌트가 언마운트될 때 웹소켓 연결 해제
    useEffect(() => {
        return () => {
            if (stompClient && stompClient.connected) {
                stompClient.disconnect(() => {
                    console.log("웹소켓 끊김");
                });
            }
        };
    }, [stompClient]);

    const sendMessage = () => {
        if (stompClient && stompClient.connected && newMessage.trim() !== "") {
            const chatMessage = {
                chatRoomId: chatRoomId,
                sender: firstUser,
                content: newMessage,
                timestamp: new Date().toISOString()
            };

            stompClient.send(`/app/chat.room/${chatRoomId}/sendMessage`, {},
                JSON.stringify(chatMessage));
            setNewMessage("");
        } else {
            console.log("메시지를 전송할수 없습니다 : Chat.jsx sendMessage");
        }
    };

    return (
        <div className="chatForm">
            <div className="messagesContainer">
                {messageList.map((msg, index) => (
                    <div className={`chatContent ${msg.sender === firstUser ? "me" : "them"}`} key={index}>
                        {msg.sender !== firstUser && <img src="/images/userImage.png" alt={`${msg.sender}`}/>}
                        <div className="messageArea">
                            <div className="messageInfo">
                                <div className="sender">{msg.sender === firstUser ? `` : msg.sender}</div>
                                <div className="content">{msg.content}</div>
                                <div
                                    className="timestamp">{msg.timestamp ? new Date(msg.timestamp).toLocaleString() : 'N/A'}</div>
                            </div>
                        </div>
                    </div>
                ))}
                <div ref={endOfMessagesRef}/>
                {/* 마지막 메시지 위치의 레퍼런스 */}
            </div>
            <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} onKeyPress={(e) => {
                if (e.key === "Enter") {
                    sendMessage();
                }
            }}/>
            <button id="chatBtn" onClick={sendMessage}>보내기</button>
        </div>
    );
}

export default Chat;




