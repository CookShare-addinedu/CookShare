import {Link} from 'react-router-dom';


const ChatButton = ({postId}) => {
    const user1Index = 11;
    const user2Index = 12;
    const foodId = 13;
    const chatRoomIdentifier = `${user1Index}_${user2Index}_${foodId}`;

    return (
        <Link to={`/chat/GetChat/${chatRoomIdentifier}`} className="chatLink">
            채팅하기 버튼
        </Link>
    );
};

export default ChatButton;