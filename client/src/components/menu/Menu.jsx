import {NavLink} from "react-router-dom";
import './Menu.scss';

export default function Menu() {
    return(
        <div className={'menu'}>
            <ul>
                <li className={'menu_list'}>
                    <NavLink to={'/main/add'}>수정</NavLink>
                </li>
                <li className={'menu_list'}>
                    <NavLink to={'/main'}>삭제</NavLink>
                </li>
            </ul>
        </div>
    )
}