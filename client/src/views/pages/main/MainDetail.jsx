import './MainDetail.scss';
import { Avatar } from "rsuite";
import {useEffect, useState} from "react";
import axios from "axios";
import {NavLink, useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import Caution from "../../../components/caution/Caution";
import CautionData from "../../../data/CautionData";
import MapView from "../../../components/address/MapView";
import {IconButton, SquareButton} from "../../../components/button/Button";
import {faAngleRight, faArrowRight} from "@fortawesome/free-solid-svg-icons";
import Drawers from "../../../components/drawer/Drawers";
import ChatButton from "../../Chat/ChatButton";

export default function MainDetail() {
    const {id} = useParams();
    const [food, setFood] = useState({ giver: {} }); // 초기 상태에 giver 객체 포함

    const fullScreenMapStyle = {
        width: '100vw',
        height: '100vh',
        position: 'fixed',
        top: 0,
        left: 0,
        zIndex: 1050 // Drawer z-index보다 높거나 같게 설정
    };

    useEffect(() => {
        const fetchFoodsData = async () => {
            try {
                const response = await axios.get(`/api/foods/${id}`);
                setFood(response.data);
            } catch (error) {
                console.error('Failed to fetch data:', error);
            }
        };

        fetchFoodsData();
    }, [id]);


    if (!food || !food.giver || !food.giver.mobileNumber) {
        return <div>Loading...</div>; // 데이터가 로드되기를 기다리는 동안 로딩 표시
    }



    console.log("giver.mobileNumber",food.giver.mobileNumber)

    return (
        <section className={'main_detail'}>
            <div className={'img_wrap'}>
                {food.imageUrls  &&
                    <img src={food.imageUrls[0]} alt={food.title}/>
                }
            </div>
            <div className={'content_wrap'}>
                <div className={'user_wrap'}>
                    <Avatar className={'avatar'} circle/>
                    <div className={'user_info'}>
                        <p className={'nick_name'}>{food.writer} 닉네임 자리입니다만</p>
                        <p className={'location'}>{food.location}주소자리</p>
                    </div>
                </div>
                <div className={'title_wrap'}>
                    <h5>{food.title}</h5>
                    <p className={'date'}>{food.createdAt}1시간전</p>
                </div>
                <div className={'dates_wrap'}>
                    <p><span>소비기한</span>{food.eatByDate}</p>
                </div>
                <div className={'description_wrap'}>
                    <p>{food.description}</p>
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
                {/*<Drawers*/}
                {/*    trigger={*/}
                {/*        <div className={'map_wrap'}>*/}
                {/*            <div className={'title_wrap'}>*/}
                {/*                <h6>나눔 희망장소</h6>*/}
                {/*                <FontAwesomeIcon icon={faAngleRight} />*/}
                {/*            </div>*/}
                {/*            <MapView/>*/}
                {/*        </div>*/}
                {/*    }*/}
                {/*    drawerContent={<MapView zoomable={true} draggable={true}/>}*/}
                {/*/>*/}
                <div className={'actions_wrap'}>
                    <section className={'main_detail'}>
                        <div className={'actions_wrap'}>
                            <ChatButton foodId={food.foodId} giverId={food.giver.mobileNumber} />
                        </div>
                    </section>
                </div>
            </div>
        </section>
    )
}



