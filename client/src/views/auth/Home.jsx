import React from 'react';
import Header from "../../components/Header";
import Button from "../../components/Button";
import './Home.css'; // CSS 파일 임포트
import { useNavigate,Routes, Route } from "react-router-dom";
import SigninSignup from "./SigninSignup";
import logo from '../../assets/img/logo.jpg';

const Home = () => {
    const navigate = useNavigate();

    const goToSignInSinUp = () => navigate('/SigninSignup');
    return (
        <div className="HomeContainer"> {/* HomeContainer 클래스 추가 */}
            <Header
                title={`푸드 쉐어 시스템 `}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
            />
            <img className={"logo"} src={logo} alt="푸드 쉐어 시스템 이미지"/>
            <Button className={"startbutton"} text={"시작하기"} onClick={goToSignInSinUp}></Button>
            <h4> 이미 계정이 있나요? </h4>
            <h4> 로그인 </h4>
        </div>
    );
}

export default Home;
