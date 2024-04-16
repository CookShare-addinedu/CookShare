import React, { useEffect, useState } from "react";
import Header from "../../../components/Header";
import Button from "../../../components/Button";
import { useNavigate } from "react-router-dom";
import './Mypage.css';
import axios from "axios";
import {upload} from "@testing-library/user-event/dist/upload";

const Mypage = () => {
    const navigate = useNavigate();

    const [inputNickName, setNickName] = useState("");
    const [inputMobileNumber, setMobileNumber] = useState("");
    const [location, setLocation] = useState("");

    const [ uploadedImage, setUploadedImage] = useState(null);
    const onChangeImage = e => {
        const file = e.target.files[0];
        const imageUrl = URL.createObjectURL(file);
        setUploadedImage(imageUrl);
        console.log(setUploadedImage);
    }

    const goToChatRoom = () => {
        navigate('/chat/GetChatList')
    }

    const chatRoomId = () => {
        navigate('/chat/GetChat/:chatRoomId')
    }


    // 사용자 정보 로드
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            axios.get("/api/user/mypage", {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
                .then(response => {
                    // 여기에서 응답 데이터를 사용하여 상태를 설정합니다.
                    setNickName(response.data.nickName);
                    setMobileNumber(response.data.mobileNumber);
                    setLocation(response.data.location);
                })
                .catch(error => {
                    console.error("마이페이지 정보 로딩 중 오류 발생:", error);
                    // 토큰이 유효하지 않거나 만료되었을 경우 로그인 페이지로 리다이렉션
                    // navigate("/login");
                    navigate("/mypage");
                });
        } else {
            console.error("토큰이 없습니다.");
            navigate("/api/user/login");
        }
    }, [navigate]);

    // 사용자 정보 업데이트 요청
    const handleUpdate = () => {
        const token = localStorage.getItem('token');
        axios.put(`/api/user/update/${inputMobileNumber}`, { // 백엔드 서버 주소가 필요합니다.
            nickName: inputNickName,
            mobileNumber: inputMobileNumber,
            location: location
        }, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                alert('정보가 성공적으로 업데이트 되었습니다.');
            })
            .catch(error => {
                console.error("정보 업데이트 중 오류 발생:", error);
            });
    };

    const handleDelete = () => {
        const token = localStorage.getItem('token');
        if (!inputMobileNumber) {
            alert("휴대폰 번호가 입력되지 않았습니다.");
            return;
        }
        axios.delete(`/api/user/delete/${inputMobileNumber}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                alert('정보가 성공적으로 삭제 되었습니다.');
                navigate('/welcome'); // 사용자를 홈페이지로 리다이렉션
            })
            .catch(error => {
                console.error("정보 삭제 중 오류 발생:", error);
            });
    };


    return (
        <div className="MyPageContainer">
            <Header title={`환영합니다 ${inputNickName}`} />
            <div className="info">
                <div className="nickName">
                    <label>프로필사진</label>
                    {uploadedImage ? (
                        <img alt={"프로필 없을 때"} src={uploadedImage}/>
                    ) : (
                        <img alt={"프로필사진"} src={"img/100.jpg"}/>
                    )}
                    <input type="file" onChange={onChangeImage}/>
                </div>
                <div className="nickName">
                    <label>닉네임:</label>
                    <input type="text" value={inputNickName} onChange={(e) => setNickName(e.target.value)}/>
                </div>
                <div className="mobileNumber">
                    <label>휴대폰번호:</label>
                    <input type="tel" value={inputMobileNumber} onChange={(e) => setMobileNumber(e.target.value)}/>
                </div>
                <div className="location">
                    <label>위치:</label>
                    <input type="text" value={location} onChange={(e) => setLocation(e.target.value)}/>
                </div>
                <div>
                    <button onClick={handleUpdate}>내정보 수정</button>
                </div>
                <div>
                    <button onClick={handleDelete}>정보 삭제</button>
                </div>
                <div>
                    <button onClick={goToChatRoom}>채팅방 목록</button>
                </div>
                <div>
                    <button onClick={chatRoomId}>채팅방</button>
                </div>
            </div>
        </div>
    );
};

export default Mypage;
