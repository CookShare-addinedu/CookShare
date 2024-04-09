import React, { useEffect, useRef, useState } from 'react';
import address from "./Address";

const DaumMapComponent = () => {
    const [address, setAddress] = useState('');
    const mapContainer = useRef(null);
    const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
    const [map, setMap] = useState(null); // 지도 인스턴스를 상태로 관리
    const [marker, setMarker] = useState(null); // 마커 인스턴스를 상태로 관리

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

    useEffect( () => {
        if(!kakaokey) {
            console.error('Kakao API key is not set in the environment variables');
            return;
        }

        const mapScript = document.createElement('script');
        mapScript.async = true;
        mapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&autoload=false`;
        document.head.appendChild(mapScript);

        mapScript.addEventListener('load', () => {
            window.kakao.maps.load(() => {
                const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
                const mapOption = {
                    center: initialPosition,
                    level: 3,
                };
                const map = new window.kakao.maps.Map(mapContainer.current, mapOption);
                const marker = new window.kakao.maps.Marker({
                    position: initialPosition,
                    map: map,
                });

                setMap(map);
                setMarker(marker);
            });
        });

        // 스크립트 언로드 시 처리 로직은 여기에 작성하지 않습니다.
    }, [kakaoKey]);




    const handleAddressSearch = () => {
        new.window.daum.postcode({
            oncomplete: function (data) {
                const addr= data.address;
                setAddress(addr);

                if(window.kakao && window.kakao.maps && window.kakao.maps.services) {
                    const geocoder = new window.kakao.maps.services.Geocoder();

                    geocoder.addressSearch(addr, function (results, status) {
                        if (status === window.kakao.maps.services.Status.OK) {
                            const result = results[0].road_address ? results[0].road_address : results[0].address;
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
            <input type={"text"} value={address} placeholder={"주소"} onChange={(e) => setAddress(e.target.value)}/>
            <input type="button" onClick={handleAddressSearch} value="주소 검색"/><br/>
            <div
                ref={mapContainer}
                style={{width: '300px',
                        height: '300px',
                        marginTop: '10px',
                        display: 'block'}}
                }}></div>
        </div>

    );
);
export default Address4;







// 지도는 뜨는데 검색 버튼 작동은 안함
//
// import React, { useEffect, useRef, useState } from 'react';
//
// const DaumMapComponent = () => {
//     const [address, setAddress] = useState('');
//     const mapContainer = useRef(null);
//     const [map, setMap] = useState(null); // 지도 인스턴스를 상태로 관리
//     const [marker, setMarker] = useState(null); // 마커 인스턴스를 상태로 관리
//     const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
//
//     useEffect(() => {
//         if (!kakaoKey) {
//             console.error('Kakao API key is not set in the environment variables');
//             return;
//         }
//
//         const mapScript = document.createElement('script');
//         mapScript.async = true;
//         mapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&autoload=false`;
//         document.head.appendChild(mapScript);
//
//         mapScript.addEventListener('load', () => {
//             window.kakao.maps.load(() => {
//                 const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
//                 const mapOption = {
//                     center: initialPosition,
//                     level: 3,
//                 };
//                 const map = new window.kakao.maps.Map(mapContainer.current, mapOption);
//                 const marker = new window.kakao.maps.Marker({
//                     position: initialPosition,
//                     map: map,
//                 });
//
//                 setMap(map);
//                 setMarker(marker);
//             });
//         });
//
//         // 스크립트 언로드 시 처리 로직은 여기에 작성하지 않습니다.
//     }, [kakaoKey]);
//
//     const handleAddressSearch = () => {
//         new window.daum.Postcode({
//             oncomplete: function(data) {
//                 setAddress(data.address); // 검색된 주소를 상태로 설정
//
//                 if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
//                     const geocoder = new window.kakao.maps.services.Geocoder();
//
//                     geocoder.addressSearch(data.address, function(results, status) {
//                         if (status === window.kakao.maps.services.Status.OK) {
//                             const result = results[0].road_address ? results[0].road_address : results[0].address;
//                             const coords = new window.kakao.maps.LatLng(result.y, result.x);
//
//                             map.setCenter(coords);
//                             marker.setPosition(coords);
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
//             <input type="button" onClick={handleAddressSearch} value="주소 검색"/><br/>
//             <br/>
//             <div
//                 ref={mapContainer}
//                 style={{width: '300px', height: '300px', marginTop: '10px', display: 'block'}}
//             ></div>
//         </div>
//     );
// };
//
// export default DaumMapComponent;


//검색창은 나오는데 맵은 안나옴
    import React, { useEffect, useRef, useState } from 'react';
    import DaumPostcode from 'react-daum-postcode';

    const DaumMapComponent = () => {
        const [address, setAddress] = useState('');
        const mapContainer = useRef(null);
        const [mapVisible, setMapVisible] = useState(true); // 지도 표시 상태
        const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
        const [map, setMap] = useState(null); // 지도 인스턴스를 상태로 관리
        const [marker, setMarker] = useState(null); // 마커 인스턴스를 상태로 관리


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
            const script = document.createElement('script');
            script.async = true;
            script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey='${kakaoKey}'&autoload=false`;
            document.head.appendChild(script);

            const onLoadKakaoMap = () => {
                window.kakao.maps.load(() => {
                    const container = document.getElementById("map");
                    const options = {
                        center: new window.kakao.maps.LatLng(37.537187, 127.005476), // 초기 중심 좌표 (위도, 경도)
                        level: 3, // 지도 확대 레벨
                    };
                    new window.kakao.maps.Map(container, options);
                });
            };
            script.addEventListener('load', onLoadKakaoMap);

            // script.addEventListener("load", () => {
            //     window.kakao.maps.load(() => {
            //         const container = document.getElementById("map");
            //         const options = {
            //             center: new window.kakao.maps.LatLng(37.537187, 127.005476), // 초기 중심 좌표 (위도, 경도)
            //             level: 3, // 지도 확대 레벨
            //         };
            //         new window.kakao.maps.Map(container, options);
            //     });
            // });
            // script.addEventListener('load', onLoadKakaoMap);
        }, []);





        //     loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
        //         .then(() => loadScript(`//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&autoload=false`))
        //         .then(() => {
        //             const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
        //             const map = new window.kakao.maps.Map(mapContainer.current, {
        //                 center: initialPosition,
        //                 level: 5,
        //             });
        //             const marker = new window.kakao.maps.Marker({
        //                 position: initialPosition,
        //                 map: map,
        //             });
        //
        //             // 마커와 지도 인스턴스를 전역 변수로 저장
        //             window.daumMap = map;
        //             window.daumMarker = marker;
        //         })
        //         .catch(error => console.error('Failed to load Daum Map scripts', error));
        // }, []);

        const handleAddressSearch = () => {
            new window.daum.DaumPostcode({
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

                                //mapContainer.current.style.display = 'block';
                                //setMapVisible(true);
                                map.relayout();
                                map.setCenter(coords);
                                marker.setPosition(coords);

                                window.daumMap.setCenter(coords);
                                window.daumMarker.setPosition(coords);
                            }
                        });
                    }
                },
            }).open();
        };

        return (
            <div>
                <input type="text" placeholder="주소" value={address} onChange={(e) => setAddress(e.target.value)}/>
                <button onClick={handleAddressSearch}>주소 검색</button>
                <br/>
                <div ref={mapContainer} style={{
                    width: '300px',
                    height: '300px',
                    marginTop: '10px'
                    //display: 'block'
                }}
                ></div>
                <div id="map" style={{width: "100%", height: "400px"}}></div>
            </div>
        );
    };

    export default DaumMapComponent;





// // 검색은 뜨는데 맵이 안뜸
//
// import React, { useEffect, useRef, useState } from 'react';
//
// const DaumMapComponent = () => {
//     const [address, setAddress] = useState('');
//     const mapContainer = useRef(null);
//     const [mapVisible, setMapVisible] = useState(true); // 지도 표시 상태
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
//     useEffect(() => {
//         if (!kakaoKey) {
//             console.error('Kakao API key is not set in the environment variables');
//             return;
//         }
//
//         loadScript('//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js')
//             .then(() => loadScript(`//dapi.kakao.com/v2/maps/sdk.js?appkey='${kakaoKey}'&autoload=false`))
//             .then(() => {
//                 const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
//                 const map = new window.kakao.maps.Map(mapContainer.current, {
//                     center: initialPosition,
//                     level: 5,
//                 });
//                 const marker = new window.kakao.maps.Marker({
//                     position: initialPosition,
//                     map: map,
//                 });
//
//                 // 마커와 지도 인스턴스를 전역 변수로 저장
//                 window.daumMap = map;
//                 window.daumMarker = marker;
//             })
//             .catch(error => console.error('Failed to load Daum Map scripts', error));
//     }, [kakaoKey]);
//
//     const handleAddressSearch = () => {
//         new window.daum.Postcode({
//             oncomplete: function(data) {
//                 const addr = data.address;
//                 setAddress(addr);
//
//                 if(window.kakao && window.kakao.maps && window.kakao.maps.services) {
//                     const geocoder = new window.kakao.maps.services.Geocoder();
//                     geocoder.addressSearch(addr, function(results, status) {
//                         if (status === window.kakao.maps.services.Status.OK) {
//                             const result = results[0];
//                             const coords = new window.kakao.maps.LatLng(result.y, result.x);
//
//                             const map = window.daumMap;
//                             const marker = window.daumMarker;
//
//                             mapContainer.current.style.display = 'block';
//                             setMapVisible(true);
//                             map.relayout();
//                             map.setCenter(coords);
//                             marker.setPosition(coords);
//                         }
//                     });
//                 }
//             },
//         }).open();
//     };
//
//     return (
//         <div>
//             <input type="text" placeholder="주소" value={address} onChange={(e) => setAddress(e.target.value)} />
//             <input type="button" onClick={handleAddressSearch} value="주소 검색" /><br />
//             <div ref={mapContainer} style={{
//                 width: '300px',
//                 height: '300px',
//                 marginTop: '10px',
//                 display: mapVisible ? 'block' : 'none'
//             }}></div>
//         </div>
//     );
// };
//
// export default DaumMapComponent;


//지도는 뜨는데 검색 버튼 작동은 안함

    import React, { useEffect, useRef, useState } from 'react';


    const DaumMapComponent = () => {
        const [address, setAddress] = useState('');
        const mapContainer = useRef(null);
        const [map, setMap] = useState(null); // 지도 인스턴스를 상태로 관리
        const [marker, setMarker] = useState(null); // 마커 인스턴스를 상태로 관리
        const kakaoKey = process.env.REACT_APP_KAKAO_KEY;

        useEffect(() => {
            if (!kakaoKey) {
                console.error('Kakao API key is not set in the environment variables');
                return;
            }

            const mapScript = document.createElement('script');
            mapScript.async = true;
            mapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&autoload=false`;
            document.head.appendChild(mapScript);

            mapScript.addEventListener('load', () => {
                window.kakao.maps.load(() => {
                    const initialPosition = new window.kakao.maps.LatLng(37.537187, 127.005476);
                    const mapOption = {
                        center: initialPosition,
                        level: 3,
                    };
                    const map = new window.kakao.maps.Map(mapContainer.current, mapOption);
                    const marker = new window.kakao.maps.Marker({
                        position: initialPosition,
                        map: map,
                    });

                    setMap(map);
                    setMarker(marker);
                });
            });

            // 스크립트 언로드 시 처리 로직은 여기에 작성하지 않습니다.
        }, [kakaoKey]);

        const handleAddressSearch = () => {
            new window.daum.Postcode({
                oncomplete: function(data) {
                    setAddress(data.address); // 검색된 주소를 상태로 설정

                    if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
                        const geocoder = new window.kakao.maps.services.Geocoder();

                        geocoder.addressSearch(data.address, function(results, status) {
                            if (status === window.kakao.maps.services.Status.OK) {
                                const result = results[0].road_address ? results[0].road_address : results[0].address;
                                const coords = new window.kakao.maps.LatLng(result.y, result.x);

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
                <input
                    type="text"
                    placeholder="주소"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                />
                <button onClick={handleAddressSearch}>주소 검색</button>
                <input type="button" onClick={handleAddressSearch} value="주소 검색"/><br/>
                <br/>
                <div
                    ref={mapContainer}
                    style={{width: '300px', height: '300px', marginTop: '10px', display: 'block'}}
                ></div>
            </div>
        );
    };

    export default DaumMapComponent;

