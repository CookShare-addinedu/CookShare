import React, {useEffect} from 'react';
import useSSEConnection from "./useSSEConnection";
import NotificationMessage from "./NotificationMessage";


const SSEComponent = () => {
    const {messages, isConnected,setMessages} = useSSEConnection();

    useEffect(() => {
        console.log("SSE Messages:", messages); // 상태 업데이트 확인
    }, [messages]);
    return (
        <div>
            {isConnected ? (
                <>
                    {messages.map((msg, index) => (
                        <NotificationMessage
                            key={index}
                            message={msg.message} /* 알림 메시지 표시 */
                            onClose={() => setMessages((prev) => prev.filter((_, i) => i !== index))} /* 닫기 */
                        />
                    ))}
                </>
            ) : (
                <div></div> 
            )}
        </div>
    );
};

export default SSEComponent;
