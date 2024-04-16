import './Header.css';
import Button from "./Button";
import React from "react";
import {useNavigate} from "react-router-dom";

const Header = ({title, leftChild, rightChild}) => {
    const navigate = useNavigate();

    const goToHome = () => navigate('/welcome');

    return (
        <div className={"Header"}>
            <div className={"header_left"}>
                <Button text={"홈으로"} onClick={goToHome} />
            </div>
            <div className={"header_center"}>{title}</div>
            {/*<div className={"header_right"}>{rightChild}</div>*/}
        </div>
    )
}

export default Header;