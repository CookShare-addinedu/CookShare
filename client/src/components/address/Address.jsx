import React, { useState } from 'react';
import AddressInput from './AddressInput';
import MapDisplay from './MapDisplay';

const Address = () => {
    const [mapUrl, setMapUrl] = useState('');

    const handleCoordinatesChange = (url) => {
        setMapUrl(url);
    };

    return (
        <div>
            <AddressInput onCoordinatesChange={handleCoordinatesChange} />
            {mapUrl && <MapDisplay mapImageUrl={mapUrl} />}
        </div>
    );
};

export default Address;
