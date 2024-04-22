import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {useNavigate, useParams} from 'react-router-dom';
import './MainDetail.scss';

function MainDetail() {
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

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this item?')) {
            try {
                await axios.delete(`/api/foods/${id}`);
                navigate('/main'); // 삭제 후 메인 페이지로 리다이렉트
                console.log("Deleted successfully!");
            } catch (error) {
                console.error('Error deleting food', error);
                setError('Failed to delete the food');
            }
        }
    };


    return (
        <div className="main-detail-container">
            {food.imageUrls && (
                <div className="main-detail-image">
                    <img src={food.imageUrls[0]} alt={food.title}/>
                </div>
            )}
            <div className="main-detail-content">
                <div className="main-detail-profile">
                    {/* 프로필 이미지와 닉네임을 여기에 추가하면 좋을 것 같습니다 */}
                </div>
                <h1 className="main-detail-title">{food.title}</h1>
                <div className="main-detail-dates">
                    <p>제조일: {food.makeByDate}</p>
                    <p>소비기한: {food.eatByDate}</p>
                </div>
                <div className="main-detail-description">
                    <p>{food.description}</p>
                </div>
                <div className="main-detail-caution">
                    <p>주의사항: {food.caution}</p>
                </div>
                <div className="main-detail-address">
                    <p>주소: {food.address}</p>
                </div>
                <div className="main-detail-map">
                    {/* 지도 컴포넌트 또는 지도 이미지를 여기에 추가 */}
                </div>
                <div className="main-detail-actions">
                    {/* 별표 이미지와 찜하기 기능을 여기에 추가 */}
                    <button className="main-detail-chat">채팅하기</button>
                    <button className="main-detail-edit" onClick={handleEdit}>Edit</button>
                    <button className="main-detail-delete" onClick={handleDelete}>Delete</button>
                </div>
            </div>
        </div>
    );
}

export default MainDetail;
