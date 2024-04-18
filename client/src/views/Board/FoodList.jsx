import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './FoodList.css';
import {Link} from "react-router-dom";

function FoodList() {
    const [foods, setFoods] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [itemsPerPage, setItemsPerPage] = useState(10);  // 페이지당 항목 수
    const [totalPages, setTotalPages] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);


    useEffect(() => {
        fetchFoods();
    }, [currentPage]);  // currentPage가 변경될 때마다 fetchFoods를 호출


    const fetchFoods = async () => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await axios.get('/api/foods', {
                params: {
                    page: currentPage - 1,  // 페이지 인덱스는 0부터 시작하므로 조정이 필요할 수 있습니다.
                    size: itemsPerPage
                }
            });
            // console.log(response.data.id);
            // console.log(response.data);
            // console.log(response.data.content);
            setFoods(response.data.content);  // 서버에서 'content' 필드에 데이터가 포함되어 있음
            setTotalPages(response.data.totalPages);  // 총 페이지 수
            setCurrentPage(response.data.number + 1);  // 현재 페이지 (응답은 0부터 시작하므로 +1 처리)
        } catch (error) {
            console.error('Failed to fetch foods', error);
            setError('Failed to load foods');
        } finally {
            setIsLoading(false);
        }
    };

    const handlePrevPage = () => {
        setCurrentPage(currentPage - 1);
    };

    const handleNextPage = () => {
        setCurrentPage(currentPage + 1);
    };
    return (
        <div className="food-list">
            {isLoading ? (
                <p>Loading...</p>
            ) : error ? (
                <p>Error: {error}</p>  // 에러가 있을 때 에러 메시지를 보여줍니다.
            ) : (
                foods.map(food => (
                    <Link to={`/foods/${food.foodId}`} key={food.foodId}>
                        <div className="food-item">
                            <h3>{food.title}</h3>
                            <p>{food.description}</p>
                            <img src={food.imageUrls[0]} alt={food.title} className="food-image" />
                        </div>
                    </Link>
                ))
            )}
            <div>
                <button onClick={handlePrevPage} disabled={currentPage === 1}>Previous</button>
                <button onClick={handleNextPage} disabled={currentPage >= totalPages}>Next</button>
            </div>
        </div>
    );

}

export default FoodList;
