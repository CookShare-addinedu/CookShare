import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse, faComments, faStore} from "@fortawesome/free-solid-svg-icons";
import {NavLink} from "react-router-dom";
import useSSEConnection from "../../Notification/useSSEConnection";
import {jwtDecode} from "jwt-decode";
import {useState} from "react";
import '../../Notification/NotificationMessage.scss'

export default function Footer() {
    const {totalChatUnreadCount} = useSSEConnection();


    return (
        <footer>
            <ul>
                <li>
                    <NavLink to={'/main'}>
                        <span><FontAwesomeIcon icon={faHouse}/></span>
                        <p>홈</p>
                    </NavLink>
                </li>
                <li>
                    {/*읽지않은 채팅 메시지 */}
                    <NavLink to={"/chat/getChatList"} style={{position: "relative"}}>
                        <span>
                            <FontAwesomeIcon icon={faComments}/>
                            {totalChatUnreadCount > 0 && (
                                <span className="chatUnread-count">{totalChatUnreadCount}</span>
                            )}
                        </span>
                        <p>채팅</p>
                    </NavLink>
                </li>
                <li>
                    <NavLink to={'/mypage'}>
                        <span><FontAwesomeIcon icon={faStore}/></span>
                        <p>나의 냉장고</p>
                    </NavLink>
                </li>
            </ul>
        </footer>
    );
}