import React, { useEffect, useRef, useState } from 'react';
import { Map } from "react-kakao-maps-sdk";
import useKakaoLoader from "./useKakaoLoader"

const Address = ({ onLocationSelect }) => {
    const [address, setAddress] = useState('');
    const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
    useKakaoLoader()

    // 외부 스크립트 로드 함수 (Promise 기반으로 수정)
    const loadScript = (src) => {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = src;
            script.onload = () => resolve();
            script.onerror = (error) => reject(error);
            document.head.appendChild(script);
        });
    };

    // 지도 및 마커 초기화 (Promise 체이닝으로 스크립트 로드 순서 보장)
    useEffect(() => {

        loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
            .then(() => {
                console.log('Daum Map scripts loaded');
            })
            .catch(error => console.error('Failed to load Daum Map scripts', error));
    }, [kakaoKey]);

    const handleAddressSearch = () => {
        new window.daum.Postcode({
            oncomplete: function(data) {
                const addr = data.address;
                setAddress(addr); //주소 상태 업데이트 하는 부분
                onLocationSelect(addr); // Register 컴포넌트로 주소 데이터 전달

            },
        }).open();
    };

    return (
        <div>
            <input type="text"
                   placeholder="주소를 입력해주세요"
                   value={address}
                   onChange={(e) => setAddress(e.target.value)}
                   onClick={handleAddressSearch}
            />
            {/*<input type="button" onClick={handleAddressSearch} value="주소 검색"/>*/}
        </div>
    );
};

export default Address;