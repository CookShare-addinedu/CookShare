import React from 'react';

const MapDisplay = ({ mapImageUrl }) => {
    return (
        <div>
            {mapImageUrl && <img src={mapImageUrl} alt="Static Map" />}
        </div>
    );
};

export default MapDisplay;
