import React from 'react';
import Header from "../../components/Header";
import Button from "../../components/Button";
import './Home.css'; // CSS 파일 임포트
import { useNavigate,Routes, Route } from "react-router-dom";
import Welcome from "./Welcome";
import logo from '../../assets/img/logo.jpg';

const Home = () => {
    const navigate = useNavigate();

    const goToWelcome= () => navigate('/Welcome');
    return (
        <div className="HomeContainer"> {/* HomeContainer 클래스 추가 */}
            <Header
                title={`Food share`}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
            />
            <img className={"logo"} src={logo} alt="푸드 쉐어 시스템 이미지"/>
            <Button className={"startbutton"} text={"시작하기"} onClick={goToWelcome}></Button>

        </div>
    );
}

export default Home;
