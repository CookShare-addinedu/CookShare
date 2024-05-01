import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse, faComments, faStore} from "@fortawesome/free-solid-svg-icons";
import {NavLink, useLocation} from "react-router-dom";
export default function Footer() {
    const location = useLocation();
    const getActiveClass = (path) => location.pathname === path ? 'active-link' : '';
    return (
        <footer>
            <ul>
                <li>
                    <NavLink className={getActiveClass('/main')} to={'/main'} >
                        <span><FontAwesomeIcon icon={faHouse} /></span>
                        <p>홈</p>
                    </NavLink>
                </li>
                <li>
                    <NavLink className={getActiveClass('/')}  to={'/'}>
                        <span><FontAwesomeIcon icon={faComments} /></span>
                        <p>채팅</p>
                    </NavLink>
                </li>
                <li>
                    <NavLink className={getActiveClass('/mypage')} to={'/mypage'}>
                        <span><FontAwesomeIcon icon={faStore}/></span>
                        <p>나의 냉장고</p>
                    </NavLink>
                </li>
            </ul>
        </footer>
    );
}