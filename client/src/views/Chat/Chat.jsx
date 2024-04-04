import React, {useEffect, useState} from "react";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import './Chat.scss'

function Chat() {
    const [stompClient, setStompClient] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");

    // const currentChatRoomId = "testRoomId";
    // const currentUser = "testUser";

    const firstUser = "User1";
    const secondUser = "User3";


    const currentChatRoomId = [firstUser, secondUser].sort().join("_");

    useEffect(() => {

        if (stompClient) {
            return;
        }

        const socket = new SockJS('http://localhost:8080/ws');
        const client = Stomp.over(socket);

        client.connect({}, (frame) => {
            console.log('연결정보: ', frame);

            client.subscribe('/topic/public', message => {
                const receivedMessage = JSON.parse(message.body);

                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });

            setStompClient(client);
        }, (error) => {
            console.error('연결에러 ', error);
        });

        return () => {
            if (client && client.connected) {
                client.disconnect(() => {
                    console.log("연결끊김");
                });
            }
        };
    }, []);

    const sendMessage = () => {
        if (stompClient && stompClient.connected && newMessage.trim() !== "") {
            const currentTime = new Date().toISOString();
            const chatMessage = {
                chatRoomId: currentChatRoomId,
                sender: firstUser,
                content: newMessage,
                timestamp: currentTime
            };

            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            console.log(chatMessage)
            setNewMessage("");
        } else {
            console.log("메시지를 보낼수 없습니돠!! 웹소켓 연결확인하고 또는 메시지 확인");
        }
    };

    return (
        <div className="chatForm">

            {messages.map((msg, index) => (
                <div className="chatContent" key={index}>
                    <div>Sender: {msg.sender}</div>
                    <div>Message: {msg.content}</div>
                    <div>Time: {msg.timestamp ? new Date(msg.timestamp).toLocaleString() : 'N/A'}</div>
                </div>
            ))}

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