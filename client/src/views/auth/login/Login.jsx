import React, {useState} from "react";
import Header from "../../../components/Header";
import Button from "../../../components/Button";
import { useNavigate } from "react-router-dom";
//import './Login.css';
import  './Login.scss';
import axios from "axios";
const Login = () => {
    const navigate = useNavigate();

    const [inputMobileNumber , setMobileNumber] = useState("");
    const [inputPassword , setPassword] = useState("");
    const [message, setMessage] = useState("");

    const loginAxios = (e) => {
        axios
            .post("/api/user/login" ,{
                mobileNumber: inputMobileNumber,
                password : inputPassword
            })
            .then((response) => {
                console.log(setMobileNumber());
                console.log(setPassword());
                console.log(response);
                localStorage.setItem('token', response.data);
                console.log(localStorage);
                const storedToken = localStorage.getItem('token');
                console.log(storedToken);
                alert("로그인 성공했습니다.")
                if ((response.status === 200)) {
                    return navigate("/mypage");
                }
            }).catch((err) => {
            setMessage(err.response.data.message)
            console.log('token');
            console.log(err);
        })
        ;
    }

    return (
        <div className="HomeContainer">
            <Header
                title={`로그인`}
            />
            <div className={"login_input"}>
                <label>휴대폰번호</label>
                <br/>
                <form>
                <input
                    type={"tel"}
                    placeholder={"휴대폰번호11자리"}
                    autoComplete={"off"}
                    onChange={(e) => {
                        setMobileNumber(e.target.value);
                    }}
                /></form>
            </div>

            <div className={"login_input"}>
                <label>비밀번호</label>
                <br/>
                <form>
                <input
                    type={"password"}
                    placeholder={"password..."}
                    autoComplete={"off"}
                    onChange={(e) => {
                        setPassword(e.target.value);
                    }}
                /></form>
            </div>
            <button text={"로그인"} type={"submit"} onClick={loginAxios}>로그인</button>
        </div>
    )
}

export default Login;
