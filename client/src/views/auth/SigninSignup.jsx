import Header from "../../components/Header";
import React from "react";
import './SigninSignup.css';
import Button, {loginButton as LoginButton, signupButton as SignupButton} from "../../components/Button";
import {useNavigate, Routes, Route} from "react-router-dom";
import SignUp from "./SignUp";

import logo from '../../assets/img/logo.jpg';

const SigninSignup = () => {
    const navigate = useNavigate();
    const goToSignIn = () => navigate('/Login');
    const goToSignUp = () => navigate('/Signup');

    return (
        <div className="HomeContainer">
            <Header
                title={`푸드 쉐어 시스템 `}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />
            <img className={"logo"} src={logo} alt="푸드 쉐어 시스템 이미지"/>
            <Button text={"로그인"} onClick={goToSignIn}></Button>
            <Button text={"회원가입"} onClick={goToSignUp}></Button>

        </div>

    )
}
export default SigninSignup;