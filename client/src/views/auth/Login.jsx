import React from "react";
import Header from "../../components/Header";
import Button from "../../components/Button";
import { useNavigate,Routes, Route } from "react-router-dom";
import './Login.css';
const Login = () => {
    const navigate = useNavigate();

    return (
        <div className="HomeContainer">
            <Header
                title={`푸드 쉐어 시스템 `}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />
            <h4> 아이디 </h4>
            <input type={"text"}/>
            <h4> 비밀번호 </h4>
            <input type={"password"}/>
            <br/>
            <br/>
            <Button text={"로그인"} type={"submit"}>로그인</Button>
        </div>
    )
}


export default Login;