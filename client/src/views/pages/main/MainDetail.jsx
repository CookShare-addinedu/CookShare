import './MainDetail.scss';
import { Avatar } from "rsuite";
import {useEffect, useState} from "react";
import axios from "axios";
import {NavLink, useNavigate, useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import Caution from "../../../components/caution/Caution";
import CautionData from "../../../data/CautionData";
import MapView from "../../../components/adress/MapView";
import {IconButton, SquareButton} from "../../../components/button/Button";
import {faAngleRight} from "@fortawesome/free-solid-svg-icons";
import {useDispatch, useSelector} from "react-redux";
import {clearFood, setFood} from "../../../redux/foodSlice";

export default function MainDetail() {
    const {id} = useParams();
    const dispatch = useDispatch();
    const foodData = useSelector((state) => state.food.value);

    useEffect(()=>{
            const fetchFoodsData = async () => {
                console.log('푸드 아이디:', id);
                try{
                    const response = await axios.get(`/api/foods/${id}`);
                    console.log('받아온 데이터 로그에 출력:', response.data);
                    dispatch(setFood(response.data));
                }catch (error){
                    dispatch(clearFood());
                    console.log('Falied to fetch data:', error);
                }
            };
            fetchFoodsData();

            return () =>{
                dispatch(clearFood());
            }
    },[id, dispatch]);

    if(!foodData){
        return <div>Loading...</div>;
    }

    return (
        <section className={'main_detail'}>
            <div className={'img_wrap'}>
                {foodData.imageUrls  &&
                    <img src={foodData.imageUrls[0]} alt={foodData.title}/>
                }
            </div>

            <div className={'content_wrap'}>
                <div className={'user_wrap'}>
                    <Avatar className={'avatar'} circle/>
                    <div className={'user_info'}>
                        <p className={'nick_name'}>{foodData.giver?.nickName || '닉네임 자리입니다만'}</p>
                        <p className={'location'}>{foodData.location}주소자리</p>
                    </div>
                </div>
                <div className={'title_wrap'}>
                    <h5>{foodData.title}</h5>
                    <p className={'date'}>{foodData.createdAt}1시간전</p>
                </div>
                <div className={'dates_wrap'}>
                    <p><span>소비기한</span>{foodData.eatByDate}</p>
                </div>
                <div className={'description_wrap'}>
                    <p>{foodData.description}</p>
                </div>
                <div className={'caution_wrap'}>
                    <Caution items={CautionData}/>
                </div>
                <div className={'map_wrap'}>
                    <div className={'title_wrap'}>
                        <h6>나눔 희망장소</h6>
                        <FontAwesomeIcon icon={faAngleRight} />
                    </div>
                    <MapView/>
                </div>

                <div className={'actions_wrap'}>
                    <IconButton icon={faHeart}/>
                    <NavLink to={'/main'}>
                        <SquareButton name={'채팅하기'}/>
                    </NavLink>
                </div>
            </div>
        </section>
    )
}