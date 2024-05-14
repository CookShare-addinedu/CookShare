import React, { useState } from 'react';
import useKakaoLoader from './useKakaoLoader';
import {Map, MapMarker} from "react-kakao-maps-sdk";

const PlaceSearch = () => {
    const [inputText, setInputText] = useState('');
    const [places, setPlaces] = useState([]);
    const [mapCenter, setMapCenter] = useState({ lat: 37.5665, lng: 126.9780 });
    const [markerPosition, setMarkerPosition] = useState(null);

    useKakaoLoader();

    const searchPlaces = () => {
        if (window.kakao && window.kakao.maps && window.kakao.maps.services) {
            const ps = new window.kakao.maps.services.Places();
            ps.keywordSearch(inputText, (data, status) => {
                if (status === window.kakao.maps.services.Status.OK) {
                    setPlaces(data);
                } else {
                    setPlaces([]);
                    alert('검색 결과가 없습니다.');
                }
            });
        } else {
            console.error("Kakao Maps libraries are not loaded yet.");
        }
    };
    const handlePlaceClick = (place) => {
        const {y, x, place_name} = place;
        const newCenter = {lat: parseFloat(y), lng: parseFloat(x)};
        setMapCenter(newCenter);
        setMarkerPosition(newCenter);
        setInputText(place_name);
    };

    return (
        <div>
            <input
                type="text"
                value={inputText}
                onChange={e => setInputText(e.target.value)}
                placeholder="Search for a place"
            />
            <button onClick={searchPlaces}>Search</button>
            <ul>
                {places.map((place, index) => (
                    <li key={index}
                        onClick={() => handlePlaceClick(place)}>
                        {place.place_name} - {place.address_name}
                    </li>
                ))}
            </ul>
            <Map
                center={mapCenter}
                style={{width: "100%", height: "350px"}}
                >
                {markerPosition && (
                    <MapMarker
                        position={markerPosition}
                    />
                )}

            </Map>
        </div>
    );
};

export default PlaceSearch;
