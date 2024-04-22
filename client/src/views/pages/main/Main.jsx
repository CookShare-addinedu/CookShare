import './Main.scss';
import Cards from "../../../components/card/Cards";
import {NavLink} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";

export default function Main() {
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
            <div className="address-map-container">
                <h2>주소</h2>
                <p>지도</p>
            </div>
            {foodData.map((food) => (
                <div className="card-container">
                    <NavLink to={`/foods/${food.foodId}`} key={food.foodId}>
                        <Cards food={food}/>
                    </NavLink>
                </div>
            ))}
            <button className="write-button">
                <NavLink to={`/add`}>글쓰기</NavLink>
            </button>
        </section>

    );
}