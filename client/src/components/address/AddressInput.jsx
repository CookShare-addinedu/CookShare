import React, { useState } from 'react';

const AddressInput = ({ onCoordinatesChange }) => {
    const [address, setAddress] = useState('');
    const [suggestions, setSuggestions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const fetchSuggestions = async () => {
        setIsLoading(true);
        setError('');
        try {
            const response = await fetch(`/suggest?query=${encodeURIComponent(address)}`);
            if (!response.ok) {
                throw new Error('Failed to fetch suggestions from server');
            }
            const data = await response.json();

            if (data.items && Array.isArray(data.items) && data.items.length > 0) {
                setSuggestions(data.items);
            } else {
                setSuggestions([]);
                setError('No suggestions found for the query.');
            }
        } catch (error) {
            console.error('Error fetching suggestions:', error);
            setError('Failed to fetch suggestions: ' + error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSelect = async (address) => {
        setIsLoading(true);
        setError('');

        try {
            const geocodeResponse = await fetch(`/geocode?address=${encodeURIComponent(address)}`);
            if (!geocodeResponse.ok) {
                const errorText = await geocodeResponse.text();
                throw new Error(`Geocode request failed: ${errorText}`);
            }

            const geocodeData = await geocodeResponse.json();
            console.log(geocodeData);
            console.log(geocodeData.latitude);
            console.log(geocodeData.longitude);
            if (!geocodeData || !geocodeData.latitude || !geocodeData.longitude){
                throw new Error('No valid coordinates returned for the address.');
            }

            const mapResponse = await fetch(`/staticmap?lat=${geocodeData.latitude}&lng=${geocodeData.longitude}`);
            if (!mapResponse.ok) {
                const errorText = await mapResponse.text();
                throw new Error(`Static map request failed: ${errorText}`);
            }

            const blob = await mapResponse.blob();
            const mapUrl = URL.createObjectURL(blob);
            onCoordinatesChange(mapUrl);
        } catch (error) {
            console.error('Error handling address selection:', error);
            setError('Error: ' + error.message);
        } finally {
            setIsLoading(false);
        }
    };


    const handleSearch = () => {
        if (address.trim() !== '') {
            fetchSuggestions();
        } else {
            setError('Please enter a valid address.');
        }
    };

    return (
        <div>
            <input
                type="text"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                placeholder="Enter an address"
                autoComplete="off"
            />
            <button onClick={handleSearch} disabled={isLoading}>
                {isLoading ? 'Loading...' : 'Search'}
            </button>
            {suggestions.length > 0 && (
                <ul>
                    {suggestions.map((suggestion, index) => (
                        <li key={index} onClick={() => handleSelect(suggestion.address)}>
                            {suggestion.title}
                        </li>
                    ))}
                </ul>
            )}
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default AddressInput;
