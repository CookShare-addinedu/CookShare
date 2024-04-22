import React, {useEffect, useState} from 'react';
import {jwtDecode} from 'jwt-decode';

const useSSEConnection = () => {
    const [isConnected, setIsConnected] = useState(false);
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        let eventSource = null;

        const createEventSource = () => {
            const token = localStorage.getItem('token');

            const decoded = jwtDecode(token);
            const userId = decoded.uid;

            const serverUrl = 'http://localhost:8080';
            const eventSource = new EventSource(`${serverUrl}/sse/connect/${encodeURIComponent(userId)}`);

            eventSource.addEventListener("connect", (event) => {
                console.log("SSE 연결 성공:", event.data);
                setIsConnected(true);
            });

            eventSource.addEventListener("notification", (event) => {
                console.log("Received notification:", event.data); // 이벤트 데이터 확인
                try {
                    const notificationData = JSON.parse(event.data);
                    setMessages((prev) => [...prev, notificationData]); // 상태 업데이트
                } catch (error) {
                    console.error("JSON 파싱 오류:", error); // 파싱 오류 처리
                }
            });

            eventSource.onerror = (error) => {
                console.error("SSE 연결 오류:", error);
                setIsConnected(false);
            };

            return eventSource;
        };

        if (!isConnected) {
            eventSource = createEventSource();
        }

        return () => {
            if (eventSource) {
                eventSource.close();
            }
        };
    }, []);

    return {messages, isConnected , setMessages};
};

export default useSSEConnection;