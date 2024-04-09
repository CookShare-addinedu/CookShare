// 검색은 뜨는데 맵이 안뜸

import React, { useEffect, useRef, useState } from 'react';

const DaumMapComponent = () => {
    const [address, setAddress] = useState('');
    const mapContainer = useRef(null);
    const [mapVisible, setMapVisible] = useState(true); // 지도 표시 상태
    const kakaoKey = process.env.REACT_APP_KAKAO_KEY;

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

    useEffect(() => {
        if (!kakaoKey) {
            console.error('Kakao API key is not set in the environment variables');
            return;
        }

        loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
            .then(() => loadScript(`//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&autoload=false`))
            .then(() => {
                const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
                const map = new window.kakao.maps.Map(mapContainer.current, {
                    center: initialPosition,
                    level: 5,
                });
                const marker = new window.kakao.maps.Marker({
                    position: initialPosition,
                    map: map,
                });

                // 마커와 지도 인스턴스를 전역 변수로 저장
                window.daumMap = map;
                window.daumMarker = marker;
            })
            .catch(error => console.error('Failed to load Daum Map scripts', error));
    }, [kakaoKey]);

    const handleAddressSearch = () => {
        new window.daum.Postcode({
            oncomplete: function(data) {
                const addr = data.address;
                setAddress(addr);

                if(window.kakao && window.kakao.maps && window.kakao.maps.services) {
                    const geocoder = new window.kakao.maps.services.Geocoder();
                    geocoder.addressSearch(addr, function(results, status) {
                        if (status === window.kakao.maps.services.Status.OK) {
                            const result = results[0];
                            const coords = new window.kakao.maps.LatLng(result.y, result.x);

                            const map = window.daumMap;
                            const marker = window.daumMarker;

                            mapContainer.current.style.display = 'block';
                            setMapVisible(true);
                            map.relayout();
                            map.setCenter(coords);
                            marker.setPosition(coords);
                        }
                    });
                }
            },
        }).open();
    };

    return (
        <div>
            <input type="text" placeholder="주소" value={address} onChange={(e) => setAddress(e.target.value)} />
            <input type="button" onClick={handleAddressSearch} value="주소 검색" /><br />
            <div ref={mapContainer} style={{
                width: '300px',
                height: '300px',
                marginTop: '10px',
                display: mapVisible ? 'block' : 'none'
            }}></div>
        </div>
    );
};

export default DaumMapComponent;
