// import React, {useEffect, useState} from 'react';// import {jwtDecode} from "jwt-decode";////// function SSEComponent() {//     const [messages, setMessages] = useState([]);//     const [isConnected, setIsConnected] = useState(false); // SSE 연결 상태//     const [reconnectAttempt, setReconnectAttempt] = useState(0); // 재연결 시도 횟수//////     const token = localStorage.getItem('token');//     const decoded = jwtDecode(token); // 토큰 디코딩//     const userId = decoded.uid;////     useEffect(() => {//         const token = localStorage.getItem('token'); // 토큰 가져오기//         if (!token) {//             console.error("토큰이 없습니다.");//             return;//         }////         const createEventSource = () => {//             const eventSource = new EventSource('/connect', {//                 withCredentials: true,//                 headers: {//                     Authorization: `Bearer ${token}`, // 토큰 포함//                 },//             });////             // SSE 연결 성공 이벤트//             eventSource.addEventListener('connect', (event) => {//                 console.log("SSE 연결 성공:", event.data);//                 setIsConnected(true);//                 setReconnectAttempt(0);//             });////             // 알림 이벤트 수신//             eventSource.addEventListener('notification', (event) => {//                 const data = JSON.parse(event.data);//                 setMessages((prev) => [...prev, data]);//             });////             // SSE 연결 오류 처리 및 재연결 로직//             eventSource.onerror = () => {//                 console.error("SSE 연결 오류");//                 setIsConnected(false);//                 if (reconnectAttempt < 5) {//                     setTimeout(() => {//                         setReconnectAttempt(reconnectAttempt + 1);//                         createEventSource();//                     }, 2000);//                 }//             };////             return eventSource;//         };////         let eventSource = null;////         if (!isConnected) {//             eventSource = createEventSource(); // SSE 연결 생성//         }////         return () => {//             if (eventSource) {//                 eventSource.close(); // 컴포넌트 언마운트 시 연결 종료//                 setIsConnected(false); // 연결 상태 업데이트//             }//         };//     }, [isConnected, reconnectAttempt]);//////     return {messages, isConnected};// };