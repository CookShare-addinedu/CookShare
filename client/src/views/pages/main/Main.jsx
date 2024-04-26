import './Main.scss';
import Cards from "../../../components/card/Cards";
import {NavLink} from "react-router-dom";
import axios from "axios";
import React, {useEffect, useState} from "react";
import ErrorBoundary from "./ErrorBoundary";

export default function Main() {
    const [searchTerm, setSearchTerm] = useState('');
    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get(`/api/foods/search?query=${encodeURIComponent(searchTerm)}`);
            setFoodData(response.data); // 검색 결과로 상태 업데이트
        } catch (error) {
            console.error('Failed to fetch search results:', error);
        }
    };

    const [foodData, setFoodData] = useState([]);
    useEffect(()=> {
        fetchData();
    },[])
    const fetchData = async () => {
        try{
            const response = await axios.get('/api/foods');
            setFoodData(response.data.content);
        }catch (error){
            console.log('Falied to fetch data:', error);
        }
    }

    return (
        <section className="main">
            <form onSubmit={handleSearch} className="search-form">
                <input
                    type="text"
                    placeholder="검색..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <button type="submit">검색</button>
            </form>


            <div className="address-map-container">
                <h2>주소</h2>
                <p>지도</p>
            </div>
            {foodData.map((food) => (
                <div className="card-container">
                    <NavLink to={`/foods/${food.foodId}`} key={food.foodId}>
                        <ErrorBoundary>
                            <Cards food={food}/>
                        </ErrorBoundary>
                    </NavLink>
                </div>
            ))}
            <button className="write-button">
                <NavLink to={`/add`}>글쓰기</NavLink>
            </button>
        </section>

    );
}