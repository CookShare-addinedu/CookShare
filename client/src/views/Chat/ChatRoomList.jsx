import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import {Link} from "react-router-dom";
import './ChatRoomList.scss'
import SockJS from "sockjs-client";
import Stomp from 'stompjs';
function ChatRoomList() {
    const [rooms, setRooms] = useState([]);
    const [selectedRooms, setSelectedRooms] = useState([]);
    const [error, setError] = useState('');


    const fetchRooms = () => {
        const token = localStorage.getItem('token');
        if (!token) {
            setError('로그인이 필요합니다.');
            return;
        }
        const decoded = jwtDecode(token);
        const userId = decoded.uid;
        axios.get('/api/ListRooms', {
            headers: {
                'Authorization': `Bearer ${token}`
            },
            params: {
                userId: userId
            }
        })
            .then(response => {
                setRooms(response.data);
            })
            .catch(error => {
                console.error('채팅방 목록을 가져오는데 실패했습니다.', error);
                setError('채팅방 목록을 가져오는데 실패했습니다.');
            });
    };


    useEffect(() => {
        fetchRooms();

        const socket = new SockJS("http://localhost:8080/ws");
        const stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {

            stompClient.subscribe("/topic/chat/room/updates", () => {
                fetchRooms();
            });
        });

        return () => {
            stompClient.disconnect()
        };


    }, []);


    const toggleRoomSelection = (chatRoomId) => {
        if (selectedRooms.includes(chatRoomId)) {
            //    console.log("if toggleRoomSelection")
            setSelectedRooms(selectedRooms.filter((id) => id !== chatRoomId)); // 선택 취소
        } else {
            //     console.log("else if toggleRoomSelection")
            setSelectedRooms([...selectedRooms, chatRoomId]); // 선택 추가
        }
    };


    const hideRooms = (chatRoomId) => {
        if (selectedRooms.length > 0 && window.confirm('선택한 채팅방을 숨기시겠습니까?')) {
            console.log("if  hideRooms")

            const token = localStorage.getItem('token');
            const decoded = jwtDecode(token);
            const userId = decoded.uid;

            selectedRooms.forEach((chatRoomId) => {
                axios.put(`/api/hideRoom/${chatRoomId}`, null, {
                    params: {userId: userId},
                    headers: {'Authorization': `Bearer ${token}`}
                })
                    .then(() => {
                        fetchRooms();
                        //      console.log('채팅방 숨김:', chatRoomId);
                    })

                    .catch(error => {
                        console.error('채팅방 숨기기에 실패했습니다.', error);
                    });

            });
            setSelectedRooms([]);
        }
    };


    if (error) {
        return <div>오류: {error}</div>;
    }


    return (
        <div className="chat-room-list-container">
            <h1>채팅방 목록</h1>
            <button onClick={hideRooms}>선택한 채팅방 숨기기</button>
            {/* 버튼 클릭 이벤트 */}
            <ul className="chat-room-list">
                {rooms.map(chatRoom => (
                    <li key={chatRoom.chatRoomId} className="chat-room">
                        <input
                            type="checkbox"
                            checked={selectedRooms.includes(chatRoom.chatRoomId)}
                            onChange={() => toggleRoomSelection(chatRoom.chatRoomId)}
                        />
                        <Link to={`/chat/GetChat/${chatRoom.chatRoomId}`} className="chat-room-link">
                            <img src="/images/userImage.png" alt="User" className="chat-room-image"/>
                            <div className="chat-room-info">
                                <p className="chat-room-name">{chatRoom.chatRoomId}</p>
                                <p className="chat-room-last-message">{chatRoom.lastMessage}</p>
                                <p className="chat-room-time">{new Date(chatRoom.lastMessageTimestamp).toLocaleString()}</p>
                            </div>
                        </Link>
                        <div className="chat-room-actions">
                            {chatRoom.unreadCount > 0 && (
                                <div className="chat-room-unread-indicator">
                                    {chatRoom.unreadCount}
                                </div>
                            )}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );

}

export default ChatRoomList;
