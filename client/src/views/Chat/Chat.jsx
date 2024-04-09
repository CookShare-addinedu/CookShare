import React, {useEffect, useState} from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.scss';
import {jwtDecode} from 'jwt-decode';
import useMessageList from "./useMessageList";

function Chat() {
    const [stompClient, setStompClient] = useState(null);
    const [newMessage, setNewMessage] = useState("");

    const token = localStorage.getItem('token');
    const decoded = jwtDecode(token);
    const firstUser = decoded.uid;

    const currentChatRoomId = "01012345678_01012345678";


    //const [messageList, setMessageList] = useMessageList(currentChatRoomId, firstUser);
    const {messageList, addMessageList } = useMessageList(currentChatRoomId, firstUser);

    useEffect(() => {
        if (stompClient) {
            return;
        }
        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, () => {
            console.log('웹소켓 연결');
            client.subscribe(`/topic/chat/room/${currentChatRoomId}`, (message) => {
                const receivedMessage = JSON.parse(message.body);

                addMessageList (receivedMessage);
            });

            setStompClient(client);
        }, (error) => {
            console.error('웹소켓 연결에러: ', error);
        });


    }, [currentChatRoomId, addMessageList ]);


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
                chatRoomId: currentChatRoomId,
                sender: firstUser,
                content: newMessage,
                timestamp: new Date().toISOString()
            };

            stompClient.send(`/app/chat.room/${currentChatRoomId}/sendMessage`, {}, JSON.stringify(chatMessage));
            setNewMessage("");
        } else {
            console.log("메시지를 전송할수 없습니다 : Chat.jsx sendMessage");
        }
    };


    return (
        <div className="chatForm">
            <div className="messagesContainer">
                {messageList.map((msg, index) => (
                    <div
                        className={`chatContent ${msg.sender === firstUser ? "me" : "them"}`}
                        key={index}
                    >
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




