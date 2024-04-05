import React from "react";
import Header from "../../components/Header";
import Button from "../../components/Button";
import { useNavigate,Routes, Route } from "react-router-dom";
import './SignUp.css';

const SignUp= () => {
    const navigate = useNavigate();

    return (
        <div className="HomeContainer">
            <Header
                title={`푸드 쉐어 시스템 `}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />
            <h1> 회원가입</h1>
            <h4> 아이디 </h4>
            <input type={"text"}/>
            <button>중복체크</button>
            <h4> 비밀번호 </h4>
            <input type={"password"}/>
            <h4> 비밀번호 확인 </h4>
            <input type={"password"}/>
            <h4> 이메일 </h4>
            <input type={"text"}/>
            <h4> 닉네임 </h4>
            <input type={"text"}/>
            <h4> 주소 </h4>
            <input type={"text"}/>
            <h4> 휴대폰번호 </h4>
            <input type={"tel"}/>
            <button>번호전송</button>
            <h4> 인증번호입력 </h4>
            <input type={"number"}/>
            <button>인증번호확인</button>
            <br/>
            <br/>
            <Button text={"가입하기"} type={"submit"}>회원 가입하기</Button>
        </div>
    )


}

export default SignUp;