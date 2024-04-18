import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {useNavigate, useParams} from 'react-router-dom';

function FoodDetail() {
    const { id } = useParams();
    const [food, setFood] = useState({});
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        console.log("Requested ID:", id); // 디버깅을 위해 요청 ID 출력
        const fetchFood = async () => {
            setIsLoading(true);
            try {
                const response = await axios.get(`/api/foods/${id}`);
                setFood(response.data);
                console.log("id 값으로 읽어와!");
                console.log(response.data);
            } catch (error) {
                console.error('Failed to fetch food details', error);
                setError('Failed to load food details');
            } finally {
                setIsLoading(false);
            }
        };

        fetchFood();
    }, [id]);

    if (isLoading) return <p>Loading...</p>;
    if (error) return <p>{error}</p>;

    const handleEdit = () => {
        navigate(`/edit-food/${id}`, { state: { food: food } });
    };

    return (
        <div>
            <h2>{food.title}</h2>
            <p>{food.description}</p>
            {food.imageUrls && <img src={food.imageUrls[0]} alt={food.title}/>}
            <button onClick={handleEdit}>Edit</button>
        </div>
    );
}

export default FoodDetail;
