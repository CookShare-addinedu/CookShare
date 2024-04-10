import React, {useState} from "react";
import Header from "../../../components/Header";
import Button from "../../../components/Button";
import {useNavigate} from "react-router-dom";
import './Register.css';
import axios from "axios";
import Address from "./Address";



const Register = () => {
    const navigate = useNavigate();

    const [inputNickName, setNickName] = useState("");
    const [nickNameCheck, setNickNameCheck] = useState(null);
    //const [password, setPassword] = useState("");
    const [inputPassword, setPassword] = useState("");
    const [inputconfirmPassword, setConfirmPassword] = useState("");
    const [isPasswordMatch, setIsPasswordMatch] = useState(null);

    const [inputMobileNumber, setMobileNumber] = useState("");
    const [verificationCode, setVerificationCode] = useState("");
    const [userInputCode, setUserInputCode] = useState("");
    const [verificationNumberCheck, setVerificationNumberCheck] = useState(null);
    // Address 컴퍼넌트에서 location 값을 받아서 업데이트 하고 받는 기능
    const [location, setLocation] = useState("");
    const handleLocationSelect = (addr) => {
        setLocation(addr); // Address 컴포넌트로부터 받은 주소로 location 상태를 업데이트합니다.
    };


    const [message, setMessage] = useState("");

    const registerAxios = (e) => {
        // e.preventDefault();
        const createdAt = new  Date().toISOString();
        const updatedAt = new Date().toISOString();
        axios
            .post("/api/user/register", {
                mobileNumber: inputMobileNumber,
                password: inputPassword,
                nickName: inputNickName,
                role: "user",
                createdAt: createdAt,
                updatedAt: updatedAt,
                location : location
            })
            .then((response) => {
                console.log(response);
                alert("회원가입 성공");
                if ((response.status = 200)) {
                    return navigate("/api/users");
                }
            }).catch((err) => {
            setMessage(err.response.data.message)
            console.log(err);
        })
        ;
    };

    const checkNickName = () => {
        // 닉네임 중복 체크 로직 구현
        axios.get(`/api/user/checkNickname?nickname=${inputNickName}`)
            .then((response) => {
                // 중복되지 않았을 경우의 처리
                console.log(response);
                setNickNameCheck(false); // 중복되지 않음
            })
            .catch((error) => {
                // 중복일 경우의 처리
                console.error(error);
                setNickNameCheck(true); // 중복됨
            });
    };

    // 비밀번호 입력 시 상태 업데이트
    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        // 비밀번호를 입력할 때마다 비밀번호 확인 상태를 리셋
        setIsPasswordMatch(null);
    };

    // 비밀번호 확인 입력 시 상태 업데이트
    const handleConfirmPasswordChange = (e) => {
        setConfirmPassword(e.target.value);
        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        setIsPasswordMatch(e.target.value === inputPassword);
    };

    // 인증번호 전송
    const sendVerificationCode = () => {
        axios.post('/memberPhoneCheck', JSON.stringify({ "to": inputMobileNumber }), {
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then((response) => {
                console.log(response.data);
                const checkNum = response.data.checkNum;
                setVerificationCode(checkNum); // 서버로부터 받은 인증번호를 상태에 저장
                if (checkNum !== undefined) {
                    alert('6자리 인증번호를 발송했습니다');
                } else {
                    alert('서버 응답에 인증번호가 없습니다.');
                }
            })
            .catch((error) => {
                console.error(error);
            });
    };

    // 인증번호 확인
    const checkVerificationCode = () => {
        if (verificationCode === userInputCode) {
            alert('인증 성공하였습니다.');
            setVerificationNumberCheck(true);

        } else {
            alert('인증 실패하였습니다. 다시 입력해주세요.');
            setVerificationNumberCheck(false);
        }
    };






    return (
        <div className="HomeContainer">
            <Header
                title={`회원가입 `}
                //     leftChild={<Button text={"<"} onClick={onDecreaseMonth} />}
                //     rightChild={<Button text={">"} onClick={onIncreaseMonth}/>}
                //
            />

            <div className={"signup_input"}>
                <label>닉네임</label>
                <br/>
                <input
                    type={"text"}
                    placeholder={"닉네임을 입력해주세요"}
                    onChange={(e) => {
                        setNickName(e.target.value);
                        setNickNameCheck(null);
                    }}
                />
            </div>
            <div className={"verificationNumber"}>
                <Button text={"중복체크"} type={"submit"} onClick={checkNickName}>중복체크</Button>
                {nickNameCheck === false && <div style={{color: 'green'}}>사용할 수 있는 닉네임입니다.</div>}
                {nickNameCheck === true && <div style={{color: 'red'}}>중복된 닉네임입니다. 다른 이름으로 바꿔주세요</div>}

            </div>


            <div className={"signup_input"}>
                <label>비밀번호</label>
                <br/>
                <form>
                    <input
                        type={"password"}
                        placeholder={"비밀번호를 입력해주세요"}
                        value={inputPassword}
                        autoComplete={"off"}
                        // onChange={(e) => {
                        //     setPassword(e.target.value);
                        // }}
                        onChange={handlePasswordChange}
                    /></form>
            </div>

            <div className={"signup_input"}>
                <label>비밀번호</label>
                <br/>
                <form>
                    <input
                        type={"password"}
                        placeholder={"비밀번호를 확인해주세요"}
                        value={inputconfirmPassword}
                        autoComplete={"off"}
                        // onChange={(e) => {
                        //     setPassword(e.target.value);
                        // }}
                        onChange={handleConfirmPasswordChange}
                    /></form>
                {isPasswordMatch === false && <div style={{color: 'red'}}>비밀번호를 다시 확인해주세요.</div>}
                {isPasswordMatch === true && <div style={{color: 'green'}}>비밀번호가 확인되었습니다.</div>}
            </div>


            <div className={"signup_input"}>
                <label>휴대폰번호</label>
                <br/>
                <input
                    type={"tel"}
                    pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}"
                    placeholder="010-1234-5678"
                    value={inputMobileNumber}
                    onChange={(e) => {
                        setMobileNumber(e.target.value);
                    }}
                />
            </div>
            <div className={"verificationNumber"}>
                <button type="button" id="sendCodeButton" onClick={sendVerificationCode}>인증번호전송</button>
            </div>

            <div className={"signup_input"}>
                <label>인증번호</label>
                <br/>
                <input
                    type={"tel"}
                    placeholder={"인증번호6자리"}
                    value={userInputCode}
                    onChange={(e) => {
                        setUserInputCode(e.target.value);
                    }}
                />
            </div>
            <div className={"verificationNumber"}>
                <button type="button" id="checkCodeButton" onClick={checkVerificationCode}>인증번호확인</button>
            </div>
            {verificationNumberCheck === true && <div style={{color: 'green'}}>본인인증 되었습니다.</div>}
            {verificationNumberCheck === false && <div style={{color: 'red'}}>다시 인증해주세요.</div>}


            {/*<div className={"signup_input"}>*/}
            {/*    <label>약관동의</label>*/}
            {/*    <br/>*/}
            {/*    <checkbox*/}
            {/*        type={"text"}*/}
            {/*        placeholder={"약관 동의"}*/}
            {/*        onChange={(e) => {*/}
            {/*            setNickName(e.target.value);*/}
            {/*        }}*/}
            {/*    />*/}
            {/*</div>*/}


            <Address onLocationSelect={handleLocationSelect} />
            {/* Address 컴포넌트에 주소 검색 결과 처리 함수를 전달합니다. */}


            <Button text={"가입하기"} type={"submit"} onClick={registerAxios}>회원 가입하기</Button>

        </div> //<div className="HomeContainer">
    ) // return


} //const Register = () => {

export default Register;