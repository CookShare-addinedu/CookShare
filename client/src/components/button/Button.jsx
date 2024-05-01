import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPencil} from "@fortawesome/free-solid-svg-icons";
import './Button.scss';
import {NavLink} from "react-router-dom";
function MypageButton({path, name, icon}) {
    return (
        <button className={'mypage_button'} path={path}>
            <div className={'btn_wrap'}>
                <FontAwesomeIcon icon={icon} />
                <p>{name}</p>
            </div>
        </button>
    )
}

function AddButton (){
    return(
        <button className={'add_button'}>
            <FontAwesomeIcon icon={faPencil} />
        </button>
    )
}

function SquareButton({name}) {
    return (
        <button className={'square_button'}>
            <span>{name}</span>
        </button>
    )
}
// IconButton 컴포넌트 수정
function IconButton({ icon, onClick, style }) {
    return (
        <button className={'icon_button'} style={style} onClick={onClick}>
            <FontAwesomeIcon icon={icon}/>
        </button>
    );
}


export {MypageButton, AddButton, SquareButton, IconButton};