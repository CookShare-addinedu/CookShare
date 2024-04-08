import React, {useState} from "react";
import Header from "../../components/Header";
import Button from "../../components/Button";
import { useNavigate } from "react-router-dom";
import './Login.css';
import axios from "axios";
const Login = () => {
    const navigate = useNavigate();

    const [inputMobileNumber , setMobileNumber] = useState("");
    const [inputPassword , setPassword] = useState("");
    const [message, setMessage] = useState("");

    const loginAxios = (e) => {
        axios
            .post("api/user/login" ,{
                mobileNumber: inputMobileNumber,
                password : inputPassword
            })
            .then((response) => {
                console.log(setMobileNumber());
                console.log(setPassword());
                console.log(response);
                alert("로그인 성공했습니다.")
                if ((response.status = 200)) {
                    localStorage.setItem('token', response.data.token); // 여기 JWT 토큰 저장 코드 추가 부분이에용~
                    localStorage.setItem('mobileNumber', response.data.mobileNumber); //  여기  모바일 번호 저장 추가 부분이에용~

                    // 저장된 값 로그로 확인
                    console.log('저장된 토큰:', response.data.token);
                    console.log('저장된 모바일 번호:', response.data.mobileNumber);
                    return navigate("/api/user/profile");


                }
            }).catch((err) => {
                setMessage(err.response.data.message)
                console.log(err);
            })
            ;
    }

    return (
        <div className="HomeContainer">
            <Header
                title={`로그인`}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />
            <div className={"login_input"}>
                <label>휴대폰번호</label>
                <br/>

                <input
                    type={"tel"}
                    placeholder={"휴대폰번호11자리"}
                    onChange={(e) => {
                        setMobileNumber(e.target.value);
                    }}
                />
            </div>

            <div className={"login_input"}>
                <label>비밀번호</label>
                <br/>
                <input
                    type={"password"}
                    placeholder={"password..."}
                    onChange={(e) => {
                        setPassword(e.target.value);
                    }}
                />
            </div>


                <Button text={"로그인"} type={"submit"} onClick={loginAxios}>로그인</Button>
            </div>
            )
            }


            export default Login;