import Header from "../../components/Header";
import React from "react";
import './Register.css';
import Button, {loginButton as LoginButton, signupButton as SignupButton} from "../../components/Button";
import {useNavigate, Routes, Route} from "react-router-dom";
import Register from "./Register";

import logo from '../../assets/img/logo.jpg';

const Welcome = () => {
    const navigate = useNavigate();
    const goToSignIn = () => navigate('/Login');
    const goToRegister = () => navigate('/Register');

    return (
        <div className="HomeContainer">
            <Header
                title={`Food share`}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />
            <img className={"logo"} src={logo} alt="푸드 쉐어 시스템 이미지"/>
            <h4> 이미 계정이 있나요? </h4>
            <Button text={"로그인"} onClick={goToSignIn}></Button>
            <h4> 계정이 없다면 회원가입 해주세요 </h4>
            <Button text={"회원가입"} onClick={goToRegister}></Button>

        </div>

    )
}
export default Welcome;