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
        <section className={'main'}>
            주소
            <p>지도</p>
            {foodData.map((food) => (
                <NavLink to={`/foods/${food.foodId}`} key={food.foodId}>
                    <Cards food={food}/>
                </NavLink>
                
            ))}
            <button className="write-button">
                <NavLink to={`/add`}>글쓰기</NavLink>
            </button>
        </section>
    );
}