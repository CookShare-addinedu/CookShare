import { useEffect } from 'react';

const useNaverMapLoader = (clientId) => {
    useEffect(() => {
        const scriptId = 'naver-maps-script';

        if (!document.getElementById(scriptId)) {
            const script = document.createElement('script');
            script.id = scriptId;
            script.src = `https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${clientId}&submodules=geocoder`;
            document.head.appendChild(script);

            script.onload = () => {
                console.log('Naver Maps script loaded successfully.');
            };

            script.onerror = (error) => {
                console.error('Error loading Naver Maps script:', error);
            };
        }
    }, [clientId]);
};

export default useNaverMapLoader;
