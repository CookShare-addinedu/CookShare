import React, {useCallback, useEffect, useRef, useState} from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.scss';
import {jwtDecode} from 'jwt-decode';
import useChatDetailList from "./useChatDetailList";
import {useParams} from "react-router-dom";
import {useScrollManagement} from "./useScrollManagement";


function Chat() {
    const {chatRoomId} = useParams(); // URL에서 roomId 추출 . app.js ChatRoomList
    const [stompClient, setStompClient] = useState(null);
    const [newMessage, setNewMessage] = useState("");

    const [isFirstLoaded, setIsFirstLoaded] = useState(true);
    const [lastMessageTimestamp, setLastMessageTimestamp] = useState(null); // 마지막으로 전송된 메시지의 ID 또는 타임스탬프를 저장
    const [hasUserScrolled, setHasUserScrolled] = useState(false);
    const messagesContainerRef = useRef(null);

    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const userId = decoded.uid;

    const {messageList, isLoading, loadChatMessages, hasMore, addMessageList} = useChatDetailList(chatRoomId, userId);

    useScrollManagement(messagesContainerRef, messageList, hasMore, isLoading,
        loadChatMessages, isFirstLoaded, setIsFirstLoaded, lastMessageTimestamp,
        hasUserScrolled, setHasUserScrolled, userId);


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

    useEffect(() => {
        loadChatMessages();  // 컴포넌트 마운트 시 초기 메시지 로드
    }, []);


    const sendMessage = () => {
        if (stompClient && stompClient.connected && newMessage.trim() !== "") {
            const timestamp = new Date().toISOString();
            const chatMessage = {
                chatRoomId: chatRoomId,
                sender: userId,
                content: newMessage,
                timestamp: timestamp
            };

            stompClient.send(`/app/chat.room/${chatRoomId}/sendMessage`, {},
                JSON.stringify(chatMessage));
            setLastMessageTimestamp(timestamp);
            setNewMessage("");


        } else {
            console.log("메시지를 전송할수 없습니다 : Chat.jsx sendMessage");
        }
    };


    return (
        <div className="chatForm">
            <div>{isLoading && <div>Loading...</div>}</div>
            <div className="messagesContainer" id="messagesContainer" ref={messagesContainerRef}>
                {messageList.map((msg, index) => (
                    <div key={index} className={`chatContent ${msg.sender === userId ? "me" : "them"}`}>
                        <div className="messageArea">
                            <div className="messageInfo">
                                <div className="sender">{msg.sender === userId ? "" : msg.sender}</div>
                                <div className="content">{msg.content}</div>
                                <div
                                    className="timestamp">{msg.timestamp ? new Date(msg.timestamp).toLocaleString() : 'N/A'}</div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} onKeyPress={(e) => {
                if (e.key === "Enter") {
                    sendMessage();
                }
            }}/>
            <button id="chatBtn" onClick={sendMessage}>메시지 전송</button>
        </div>
    );
}

export default Chat;





