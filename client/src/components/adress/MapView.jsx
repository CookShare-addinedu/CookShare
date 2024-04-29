import React, { useEffect, useRef, useState } from 'react';
import {CustomOverlayMap, Map, MapMarker} from "react-kakao-maps-sdk"
import useKakaoLoader from "./useKakaoLoader"

const MapView = () => {
    const [zoomable, setZoomable] = useState(false);
    const [draggable, setDraggable] = useState(false);
    useKakaoLoader();
    // const mapContainer = useRef(null);
    // const [mapLoaded, setMapLoaded] = useState(false);

    // useEffect(() => {
    //     const loadScript = (src) => {
    //         return new Promise((resolve, reject) => {
    //             const script = document.createElement('script');
    //             script.src = src;
    //             script.onload = () => resolve();
    //             script.onerror = (error) => reject(error);
    //             document.head.appendChild(script);
    //         });
    //     };

    // const kakaoKey = process.env.REACT_APP_KAKAO_KEY;
    // const kakaoSdkUrl = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoKey}&libraries=services&autoload=false`;
    // console.log("Loading Kakao Maps SDK with URL: ", kakaoSdkUrl);

    //     loadScript(kakaoSdkUrl)
    //         .then(() => {
    //             window.kakao.maps.load(() => {
    //                 const center = new window.kakao.maps.LatLng(37.5665, 126.9780);
    //                 const map = new window.kakao.maps.Map(mapContainer.current, {
    //                     center: center,
    //                     level: 3,
    //                 });
    //                 setMapLoaded(true);
    //             });
    //         })
    //         .catch(error => console.error('Failed to load Kakao Map scripts', error));
    // }, []);

    return (
        <>
            {/*<MarkerWithCustomOverlayStyle />*/}
            <Map // 지도를 표시할 Container
                center={{
                    // 지도의 중심좌표
                    lat: 37.54699,
                    lng: 127.09598,
                }}
                style={{
                    // 지도의 크기
                    width: "100%",
                    height: "350px",
                }}
                level={3} // 지도의 확대 레벨
                zoomable={zoomable}
                draggable={draggable}
            >
                <MapMarker className="marker"// 마커를 생성합니다
                    position={{ lat: 37.54699, lng: 127.09598 }}
                    image={{
                        src: "/img/locationdot.svg", // 마커이미지의 주소입니다
                        size: {
                            width: 34,
                            height: 39,
                        }, // 마커이미지의 크기입니다
                        options: {
                            offset: {
                                x: 27,
                                y: 44,
                            }, // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
                        },
                    }}
                />
                <CustomOverlayMap
                    position={{ lat: 37.54699, lng: 127.09598 }}
                    yAnchor={3.5}
                >
                    {/*<div className="customoverlay">*/}
                    {/*    <span className="title">구의야구공원</span>*/}
                    {/*</div>*/}
                </CustomOverlayMap>
            </Map>
        </>
    );
};

export default MapView;
