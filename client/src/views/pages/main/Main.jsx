import './Main.scss';
import Cards from "../../../components/card/Cards";
import {NavLink} from "react-router-dom";
import axios from "axios";
import React, {useEffect, useRef, useState} from "react";
import {Tabs} from "rsuite";

export default function Main() {
    const [FoodData, setFoodData] = useState([]);
    const [Page, setPage] = useState(1);
    const [localData, setLocalData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);
    const [Panel, setPanel] = useState([
        { key: '1', title: '나눔거래' },
        { key: '2', title: '나의동네' }
    ]);
    const loader = useRef(null);

    useEffect(()=> {
        fetchFoodsData();
        const observer = new IntersectionObserver(handleObserver,{thresholds: [0.5]});
        if(loader.current){
            observer.observe(loader.current);
        }
        return () => {
            if(loader.current){
                observer.unobserve(loader.current);
            }
        };
    },[]);

    const handleObserver = (entries) => {
        const target = entries[0];
        if(target.isIntersecting && !loading && hasMore){
            fetchFoodsData();
        }
    }
    const fetchFoodsData = async () => {
        if(!hasMore || loading) return;

        setLoading(true);
        try{
            const response = await axios.get('/api/foods');
            if(response.data.content.length === 0){
                setHasMore(false);
            }else{
                setFoodData(prev => [...prev, ...response.data.content]);
                setPage(prev => prev + 1);
            }
        }catch (error){
            console.log('Falied to fetch data:', error);
        }finally {
            setLoading(false);
        }
    }

    return (
        <section className={'main'}>
            <Tabs className={'main_tab'} defaultActiveKey="1" appearance="subtle">
                {Panel.map((tab) => (
                    <Tabs.Tab className={'tab'} eventKey={tab.key} title={tab.title} key={tab.key}>
                        <div className={'tab_content'}>
                            {tab.key === '1' ?
                                FoodData.map((food) => (
                                    <NavLink to={`foods/${food.foodId}`} key={food.foodId}>
                                        <Cards food={food}/>
                                    </NavLink>
                                )) : '나랑 시켜먹을래?'}
                        </div>
                        )
                    </Tabs.Tab>
                ))}
            </Tabs>
        </section>
    );
}