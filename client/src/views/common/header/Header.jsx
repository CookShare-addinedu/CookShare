import './Header.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft, faEllipsisVertical, faMagnifyingGlass, faBell} from "@fortawesome/free-solid-svg-icons";
import useHeaderTitle from "../../../hook/useHeaderTitle";
import {NavLink, useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";
import useSSEConnection from "../../Notification/useSSEConnection";
import '../../Notification/NotificationMessage.scss'
function Header1() {
    const navigate = useNavigate();
    const title = useHeaderTitle();

    return (
        <header className={'header1'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft}/>
                    </span>
                </button>
            </div>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
        </header>
    );
}

function Header2() {
    const title = useHeaderTitle();
    return (
        <header className={'header2'}>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
        </header>
    )
}
function Header3() {
    const navigate = useNavigate();
    const title = useHeaderTitle();
    return(
        <header className={'header3'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft}/>
                    </span>
                </button>
            </div>
            <div className={'title'}>
                <h2>{title}</h2>
            </div>
            <div className={'menu'}>
                <button>
                    <span>
                        <FontAwesomeIcon icon={faEllipsisVertical}/>
                    </span>
                </button>
            </div>
        </header>
    )
}
function Header4(){
    const address = useSelector(state => state.address.currentAddress);
    const {totalNoticeUnreadCount} = useSSEConnection();

    return(
        <header className={'header4'}>
            <div className={'address_wrap'}>
                <NavLink to={'/searchAddress'}>
                    <span className={'address'}>{address}</span>
                </NavLink>
            </div>
            <div className={'menu'}>
                <ul>
                    <li>
                        <NavLink to={'/search'}>
                            <FontAwesomeIcon icon={faMagnifyingGlass}/>
                        </NavLink>
                    </li>
                    <li>
                        {/*읽지않은 알림 메시지 */}
                        <NavLink to={'/notification'} style={{ position: "relative" }}>
                            <FontAwesomeIcon icon={faBell} />
                            {totalNoticeUnreadCount > 0 && (
                                <span className="noticeUnread-count">{totalNoticeUnreadCount}</span>
                            )}
                        </NavLink>
                    </li>
                </ul>
            </div>
        </header>
    )
}


export {Header1, Header2, Header3, Header4};