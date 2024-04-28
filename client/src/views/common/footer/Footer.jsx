import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse, faComments, faStore} from "@fortawesome/free-solid-svg-icons";
import {NavLink} from "react-router-dom";
import useSSEConnection from "../../Notification/useSSEConnection";
import {jwtDecode} from "jwt-decode";
import {useState} from "react";

export default function Footer() {
    const {totalUnreadCount} = useSSEConnection();


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
                    <NavLink to={"/chat/getChatList"} style={{position: "relative"}}>
                        <span>
                            <FontAwesomeIcon icon={faComments}/>
                            {totalUnreadCount > 0 && (
                                <span className="unread-count">{totalUnreadCount}</span>
                            )}
                        </span>
                        <p>채팅</p>
                        {totalUnreadCount}
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