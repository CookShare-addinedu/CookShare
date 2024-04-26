import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './Button.scss';
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
export {MypageButton};