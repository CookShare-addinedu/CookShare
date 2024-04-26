import {useState, useEffect, useCallback} from 'react';
import axios from 'axios';

const useChatDetailList = (chatRoomId, userId) => {
    const [messageList, setMessageList] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    const [pageSize, setPageSize] = useState(10); // 초기 페이지 사이즈는 10
    const [isLoading, setIsLoading] = useState(false);

    //메시지 페이징 조회
    const loadChatMessages = useCallback(() => {
        if (!hasMore || isLoading) {
            return Promise.resolve();
        }
        setIsLoading(true);
        return axios.get(`/api/detailRoom/${chatRoomId}/messages`, {
            //arams: { userId, page, size: 10 }
            params: {userId, page, size: pageSize}
        })
            .then(response => {
                let newMessages = [];
                if (response.data && Array.isArray(response.data.content)) {
                    newMessages = response.data.content.reverse();
                }
                if (newMessages.length < pageSize) {
                    setHasMore(false);
                }
                setMessageList(prevMessages => [...newMessages, ...prevMessages]);
                setPage(prevPage => prevPage + 1);

                if (page === 0) {
                    setPageSize(5);
                }
            })
            .catch(error => {
                console.error("채팅방 상세 정보 조회 에러: ", error);
                setHasMore(false); // 에러 발생시 더 이상 로드하지 않음
            })
            .finally(() => {
                setIsLoading(false); // 로딩 상태 종료
            });
    }, [chatRoomId, userId, page, hasMore, isLoading, pageSize]);



    const addMessageList = useCallback((newMessage) => {
        setMessageList(prevMessages => [...prevMessages, newMessage]);
    }, []);


    return {messageList, isLoading, loadChatMessages, hasMore, addMessageList};
};

export default useChatDetailList;