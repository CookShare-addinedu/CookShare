import React, {useEffect, useState} from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.scss'
import { jwtDecode } from 'jwt-decode';
import axios from "axios";

function Chat() {
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");

    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const firstUser = decoded.uid;

    const secondUser = "01012345678";
  //  const currentChatRoomId = [firstUser, secondUser].sort().join("_");
    const currentChatRoomId = "01000000000_01012345678";

    useEffect(() => {

        if (stompClient) {
            return;
        }

        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, (frame) => {
            console.log('연결정보: ', frame);

            client.subscribe(`/topic/chat/room/${currentChatRoomId}`, message => {
                const receivedMessage = JSON.parse(message.body);

                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });

            setStompClient(client);
        }, (error) => {
            console.error('연결에러 ', error);
        });

        axios.get(`/api/detailRoom/${currentChatRoomId}`, {
            params: {
                userId: firstUser
            }
        })
            .then(response => {
                const data = response.data;
                setMessages(data.content || []);
                console.log("채팅조회!!!!!!!!!!!!!!!!!!!!", data);
            })
            .catch(error => {
                console.error("채팅방 상세 정보 조회 에러: ", error);
            });

        return () => {
            if (client && client.connected) {
                client.disconnect(() => {
                    console.log("연결끊김");
                });
            }
        };
    }, [currentChatRoomId]);

    const sendMessage = () => {
        if (stompClient && stompClient.connected && newMessage.trim() !== "") {
            const currentTime = new Date().toISOString();
            const chatMessage = {
                chatRoomId: currentChatRoomId,
                sender: firstUser,
                content: newMessage,
                timestamp: currentTime
            };

            stompClient.send(`/app/chat.room/${currentChatRoomId}/sendMessage`, {}, JSON.stringify(chatMessage));
            console.log(chatMessage)
            setNewMessage("");
        } else {
            console.log("메시지를 보낼수 없습니돠!! 웹소켓 연결확인하고 또는 메시지 확인");
        }
    };

    return (
        <div className="chatForm">
            <div className="messagesContainer">
                {messages.map((msg, index) => (
                    <div
                        className={`chatContent ${msg.sender === firstUser ? "me" : "them"}`}
                        key={index}
                    >
                        {msg.sender !== firstUser && <img src="/images/userImage.png" alt={`${msg.sender}`} />}
                        <div className="messageArea">
                            <div className="messageInfo">
                                <div className="sender">{msg.sender === firstUser ? `` : msg.sender}</div>
                                <div className="content">{msg.content}</div>
                                <div className="timestamp">{msg.timestamp ? new Date(msg.timestamp).toLocaleString() : 'N/A'}</div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <input
                type="text" value={newMessage} onChange={(e) => setNewMessage(e.target.value)} onKeyPress={(e) => {
                if (e.key === "Enter") {
                    sendMessage();
                }
            }}
            />
            <button id="chatBtn" onClick={sendMessage}>보내기</button>
        </div>
    );
}

export default Chat;