import React, {useEffect, useState} from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const Notifications = () => {
    const [notifications, setNotifications] = useState([]);
    const token = localStorage.getItem('jwt');
    const decoded = jwtDecode(token);
    const [selectedNotifications, setSelectedNotifications] = useState(new Set());

    let userId;
    if (token) {
        try {
            const decoded = jwtDecode(token);
            userId = decoded.userId;
        } catch (error) {
            console.error('Invalid token', error);
            // 유효하지 않은 토큰 처리 로직 (로그아웃 등)
        }
    }


    useEffect(() => {
        axios.get('/api/notifications/unread', {
            params: {userId: userId}
        })
            .then(response => {
                console.log(response.data);
                setNotifications(response.data);
            })
            .catch(error => console.error('알림 목록 조회 실패', error));
    }, []);


    const toggleNotificationSelection = (notificationId) => {
        setSelectedNotifications(prev => {
            const newSet = new Set(prev);
            if (newSet.has(notificationId)) {
                newSet.delete(notificationId);
            } else {
                newSet.add(notificationId);
            }
            return newSet;
        });
    };


    const markSelectedAsRead = () => {
        if (window.confirm("선택한 알림을 읽음으로 처리하시겠습니까?")) {
            selectedNotifications.forEach(notificationId => {
                axios.put(`/api/notifications/updateAsRead/${notificationId}`)
                    .then(() => {
                        setNotifications(prev => prev.map(notification =>
                            notification.notificationId === notificationId ? {
                                ...notification,
                                isRead: true
                            } : notification
                        ));
                        setSelectedNotifications(prev => {
                            const newSet = new Set(prev);
                            newSet.delete(notificationId);
                            return newSet;
                        });
                    })
                    .catch(error => console.error('알림 확인 에러', error));
            });
        }
    };


    const markAllAsRead = () => {
        if (window.confirm("모든 알림을 읽음으로 처리하시겠습니까?")) {
            notifications.forEach(notification => {
                if (!notification.isRead) {
                    axios.put(`/api/notifications/updateAsRead/${notification.notificationId}`)
                        .then(() => {
                            setNotifications(prev => prev.map(notif =>
                                ({...notif, isRead: true}))
                            );
                        })
                        .catch(error => console.error('모든 알림 읽기 실패', error));
                }
            });
        }


    }

    return (
        <div>
            <h2>알림 목록</h2>
            <button onClick={markAllAsRead}>모두 읽음</button>
            <button onClick={markSelectedAsRead}>선택한 알림 읽음 처리</button>
            <ul>
                {notifications.map(notification => (
                    <li key={notification.notificationId}>
                        {notification.message}
                        <input
                            type="checkbox"
                            checked={selectedNotifications.has(notification.notificationId)}
                            onChange={() => toggleNotificationSelection(notification.notificationId)}
                            disabled={notification.isRead}
                        />
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default Notifications;