import React, {useCallback, useEffect, useRef, useState} from "react";
import './Chat.scss';
import {jwtDecode} from 'jwt-decode';
import useChatDetailList from "./useChatDetailList";
import {useLocation, useNavigate, useParams, useSearchParams} from "react-router-dom";
import {useScrollManagement} from "./useScrollManagement";
import useWebSocketConnection from "./useWebSocketConnection";
import axios from "axios";


function Chat() {
    const [newMessage, setNewMessage] = useState("");
    const [chatRoomId, setChatRoomId] = useState(null);
    const [isFirstLoaded, setIsFirstLoaded] = useState(true);
    const [lastMessageTimestamp, setLastMessageTimestamp] = useState(null); // 마지막으로 전송된 메시지의 ID 또는 타임스탬프를 저장
    const [hasUserScrolled, setHasUserScrolled] = useState(false);
    const messagesContainerRef = useRef(null);

    const [searchParams] = useSearchParams();
    const { foodId, userId } = useParams(); // URL 파라미터에서 foodId와 userId를 추출합니다.
    const tokenId = jwtDecode(localStorage.getItem('jwt')).userId;

    useEffect(() => {
        if (foodId && userId) {
            checkAndCreateChatRoom();
        }
    }, [foodId, userId]);


    const {messageList, isLoading, loadChatMessages, hasMore, addMessageList} = useChatDetailList(chatRoomId, userId);

    const stompClient = useWebSocketConnection(
        "http://localhost:8080/ws",
        `/topic/chat/room/${chatRoomId}`,
        (receivedMessage) => {
            addMessageList(receivedMessage);
        }
    );
    useScrollManagement(messagesContainerRef, messageList, hasMore, isLoading,
        loadChatMessages, isFirstLoaded, setIsFirstLoaded, lastMessageTimestamp,
        hasUserScrolled, setHasUserScrolled, userId);


    useEffect(() => {
        loadChatMessages();
    }, []);


    const checkAndCreateChatRoom = async () => {
        if (!chatRoomId) {
            try {
                console.log("채팅방 개설해주세요");
                console.log(tokenId);
                console.log(userId);
                console.log(foodId);
                const response = await axios.post('/api/chat/createRoom', {
                    firstUserMobileNumber: tokenId,
                    secondUserMobileNumber: userId,
                    foodId: foodId
                });
                setChatRoomId(response.data.chatRoomId);
            } catch (error) {
                console.error('채팅방 생성 중 오류 발생:', error);
            }
        }
    };

    const sendMessage = async () => {
        if (!chatRoomId) {
            await checkAndCreateChatRoom();
        }
        if (stompClient && stompClient.connected && newMessage.trim() !== '') {
            const timestamp = new Date().toISOString();
            const chatMessage = {
                chatRoomId: chatRoomId,
                sender: userId,
                content: newMessage,
                timestamp: timestamp,
            };

            stompClient.send(`/app/chat.room/${chatRoomId}/sendMessage`, {}, JSON.stringify(chatMessage));
            setLastMessageTimestamp(timestamp);
            setNewMessage('');
        } else {
            console.log("메시지를 전송할 수 없습니다.");
        }
    };


    return (
        <div>
            <div className="chatForm">
                {/*{isLoading && <div className="loadingContainer">Loading...</div>}*/}
                <div className="messagesContainer" id="messagesContainer" ref={messagesContainerRef}>
                    {messageList.map((msg, index) => (
                        <div key={index} className={`chatContent ${msg.sender === userId ? "me" : "them"}`}>
                            {msg.sender !== userId && <img src="/img/fooding.png" alt={`${msg.sender}`}/>}
                            <div className="messageArea">
                                <div className="messageInfo">
                                    <div className="sender">{msg.sender === userId ? "" : msg.sender}</div>
                                    <div className="content">{msg.content}</div>
                                    <div
                                        className="timestamp">
                                        {msg.timestamp ? new Date(msg.timestamp).toLocaleString('ko-KR',
                                            {
                                                year: 'numeric',
                                                month: '2-digit',
                                                day: '2-digit',
                                                hour: '2-digit',
                                                minute: '2-digit'
                                            }) : 'N/A'}

                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
                <div className="inputContainer">
                    <input type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)}
                           onKeyPress={(e) => {
                               if (e.key === "Enter") {
                                   sendMessage();
                               }
                           }}/>
                    <button id="chatBtn" onClick={sendMessage}>보내기</button>
                </div>
            </div>
        </div>
    );
}

export default Chat;





