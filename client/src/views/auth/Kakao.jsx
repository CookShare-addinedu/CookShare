// import React, { useEffect, useRef, useState } from 'react';
//
// const DaumMapComponent = () => {
//     const [address, setAddress] = useState('');
//     const mapContainer = useRef(null);
//     const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
//
//     // 외부 스크립트 로드 함수 (Promise 기반으로 수정)
//     const loadScript = (src) => {
//         return new Promise((resolve, reject) => {
//             const script = document.createElement('script');
//             script.src = src;
//             script.onload = () => resolve();
//             script.onerror = (error) => reject(error);
//             document.head.appendChild(script);
//         });
//     };
//
//     // 지도 및 마커 초기화 (Promise 체이닝으로 스크립트 로드 순서 보장)
//     useEffect(() => {
//         if (!kakaoKey) {
//             console.error('Kakao API key is not set in the environment variables');
//             return;
//         }
//
//         loadScript(`//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services,clusterer,drawing`)
//             .then(() => {
//                 const kakao = window.kakao;
//                 const initialPosition = new kakao.maps.LatLng(37.537187, 127.005476);
//                 const map = new kakao.maps.Map(mapContainer.current, {
//                     center: initialPosition,
//                     level: 5,
//                 });
//                 const marker = new kakao.maps.Marker({
//                     position: initialPosition,
//                     map: map,
//                 });
//
//                 // 마커와 지도 초기화 로직에 추가된 내용
//                 window.daumMap = map; // 전역 변수에 지도 인스턴스 저장
//                 window.daumMarker = marker; // 전역 변수에 마커 인스턴스 저장
//             })
//             .catch(error => console.error('Failed to load Daum Map scripts', error));
//     }, [kakaoKey]);
//
//     const handleAddressSearch = () => {
//         new window.daum.Postcode({
//             oncomplete: function (data) {
//                 const addr = data.address;
//                 setAddress(addr);
//
//                 // 서비스 접근 전에 daum 객체의 존재 여부를 확인
//                 if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
//                     const geocoder = new window.kakao.maps.services.Geocoder();
//                     geocoder.addressSearch(addr, function (results, status) {
//                         if (status === window.kakao.maps.services.Status.OK) {
//                             const result = results[0];
//                             const coords = new window.kakao.maps.LatLng(result.y, result.x);
//
//                             const map = window.daumMap; // 전역 변수에서 지도 인스턴스를 가져옴
//                             const marker = window.daumMarker; // 전역 변수에서 마커 인스턴스를 가져옴
//
//                             mapContainer.current.style.display = 'block'; // 지도를 보이게 상태 변경
//                             map.relayout(); // 지도 레이아웃을 다시 계산
//                             map.setCenter(coords); // 지도 중심을 변경
//                             marker.setPosition(coords); // 마커 위치를 변경
//                         }
//                     });
//                 }
//             },
//         }).open();
//     };
//
//     return (
//         <div>
//             <input
//                 type="text"
//                 placeholder="주소"
//                 value={address}
//                 onChange={(e) => setAddress(e.target.value)}
//             />
//             <button onClick={handleAddressSearch}>주소 검색</button>
//             <br />
//             <div
//                 ref={mapContainer}
//                 style={{
//                     width: '300px',
//                     height: '300px',
//                     marginTop: '10px',
//                 }}
//             ></div>
//         </div>
//     );
// };
//
// export default DaumMapComponent;
