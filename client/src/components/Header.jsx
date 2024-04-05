import './Header.css';
import Button from "./Button";
const Header =({title, leftChild, rightChild}) => {
    return(
        <div className={"Header"}>
           {/*<div className={"header_left"}>{leftChild}</div>*/}
           <div className={"header_center"}>{title}</div>
           {/*<div className={"header_right"}>{rightChild}</div>*/}
            <div className={'header_button'}>
                <Button text={"홈으로"}></Button>
            </div>
        </div>
    )
}

export default Header;