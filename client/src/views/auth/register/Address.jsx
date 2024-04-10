import React, { useEffect, useRef, useState } from 'react';
import { Map } from "react-kakao-maps-sdk"
import useKakaoLoader from "./useKakaoLoader"

const Address = ({ onLocationSelect }) => {
    const [address, setAddress] = useState('');
    const mapContainer = useRef(null);
    const [mapVisible, setMapVisible] = useState(true); // 지도 표시 상태
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
    // useEffect(() => {
    //     loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
    //         .then(() => loadScript(`//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services`))
    //         .then(() => {


    // 지도 및 마커 초기화 (Promise 체이닝으로 스크립트 로드 순서 보장)
    useEffect(() => {
        // Kakao SDK 스크립트의 URL에서 REACT_APP_KAKAO_KEY 환경 변수를 사용합니다.
        const kakaoSdkUrl = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services&autoload=false`;

        loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
            .then(() => loadScript(kakaoSdkUrl))
            .then(() => {
                const kakao = window.kakao;
                const initialPosition = new kakao.maps.LatLng(37.478120470545385, 126.88041718626472);
                const map = new kakao.maps.Map(mapContainer.current, {
                    center: initialPosition,
                    level: 5,
                });
                const marker = new kakao.maps.Marker({
                    position: initialPosition,
                    map: map,
                });

                // 마커와 지도 초기화 로직에 추가된 내용
                window.kakaoMap = map; // 전역 변수에 지도 인스턴스 저장
                //window.daumMarker = marker; // 전역 변수에 마커 인스턴스 저장
            })
            .catch(error => console.error('Failed to load Daum Map scripts', error));
    }, [kakaoKey]);

    const handleAddressSearch = () => {
        new window.daum.Postcode({
            oncomplete: function(data) {
                const addr = data.address;
                setAddress(addr); //주소 상태 업데이트 하는 부분
                onLocationSelect(addr); // Register 컴포넌트로 주소 데이터 전달
                const kakao = window.kakao;

                // 서비스 접근 전에 daum 객체의 존재 여부를 확인
                if(kakao && kakao.maps && kakao.maps.services) {
                    const geocoder = new kakao.maps.services.Geocoder();
                    geocoder.addressSearch(addr, function(results, status) {
                        if (status === kakao.maps.services.Status.OK) {
                            const result = results[0];
                            const coords = new kakao.maps.LatLng(result.y, result.x);

                            const map = window.kakaoMap; // 전역 변수에서 지도 인스턴스를 가져옴

                            // 기존 마커 제거
                            if (window.kakaoMarker) {
                                window.kakaoMarker.setMap(null);
                            }

                            // 새 마커 생성
                            const newMarker = new kakao.maps.Marker({
                                position: coords,
                                map: map,
                            });

                            // 전역 변수에 새 마커 저장
                            window.kakaoMarker = newMarker;

                            mapContainer.current.style.display = 'block';
                            setMapVisible(true); // 지도를 보이게 상태 변경
                            map.relayout(); // 지도 레이아웃을 다시 계산
                            map.setCenter(coords); // 지도 중심을 변경
                            newMarker.setPosition(coords); // 마커 위치를 변경
                        }
                    });
                }
            },
        }).open();
    };

    return (
        <div>
            {/*<script type="text/javascript"*/}
            {/*        src="//dapi.kakao.com/v2/maps/sdk.js?appkey=kakaoKey&autoload=false&libraries=services"></script>*/}

            <input type="text" placeholder="주소검색버튼 누르세요" value={address} onChange={(e) => setAddress(e.target.value)}/>
            <input type="button" onClick={handleAddressSearch} value="주소 검색"/><br/>
            <div ref={mapContainer} style={{
                width: '300px',
                height: '300px',
                marginTop: '10px',
                display: mapVisible ? 'block' : 'none'
            }}></div>
        </div>
    );
};

export default Address;