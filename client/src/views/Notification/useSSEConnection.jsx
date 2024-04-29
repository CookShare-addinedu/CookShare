import React, {useEffect, useState} from 'react';
import {jwtDecode} from 'jwt-decode';

const useSSEConnection = () => {
    const [isConnected, setIsConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [totalChatUnreadCount, setTotalChatUnreadCount] = useState(0);
    const [totalNoticeUnreadCount, setTotalNoticeUnreadCount] = useState(0);


    useEffect(() => {
        if (isConnected) {
            return;
        }

        const token = localStorage.getItem('jwt');
        if (!token) {
            console.error("No token available.");
            return;
        }

        const decoded = jwtDecode(token);
        const userId = decoded.mobileNumber;
        const serverUrl = 'http://localhost:8080';
        const eventSource = new EventSource(`${serverUrl}/sse/connect/${encodeURIComponent(userId)}`);

        eventSource.onopen = () => {
            console.log("SSE 연결 성공");
            setIsConnected(true);
        };

        const eventHandlers = {
            "updateUserStatus": event => {
                const data = JSON.parse(event.data);
                setTotalChatUnreadCount(data.unreadChatCount);
                setTotalNoticeUnreadCount(data.unreadNoticeCount);
            },
            "notification": event => {
                try {
                    const notificationData = JSON.parse(event.data);
                    setMessages(prev => [...prev, notificationData]);
                } catch (error) {
                    console.error("JSON 파싱 오류:", error);
                }
            },
            "heartbeat": event => {
                // Heartbeat handling logic if needed
            },
        };

        Object.entries(eventHandlers).forEach(([type, handler]) => {
            eventSource.addEventListener(type, handler);
        });

        eventSource.onerror = (error) => {
            console.error("SSE 연결 오류:", error);
            eventSource.close();
            setIsConnected(false);
        };

        return () => {
            eventSource.close();
            setIsConnected(false);
        };
    }, []);

    return {messages, isConnected, setMessages, totalChatUnreadCount, totalNoticeUnreadCount};
};

export default useSSEConnection;