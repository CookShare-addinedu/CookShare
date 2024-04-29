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

export default function MainDetail() {
    const {id} = useParams();
    const [Food, setFood] = useState({});
    const fullScreenMapStyle = {
        width: '100vw',
        height: '100vh',
        position: 'fixed',
        top: 0,
        left: 0,
        zIndex: 1050 // Drawer z-index보다 높거나 같게 설정
    };

    useEffect(() => {
        fetchFoodsData();
    }, []);

    const fetchFoodsData = async () => {
        console.log('푸드 아이디:', id);
        try{
            const response = await axios.get(`/api/foods/${id}`);
            console.log('받아온 데이터 로그에 출력:', response.data);
            setFood(response.data);
        }catch (error){
            console.log('Falied to fetch data:', error);
        }
    }
    return (
        <section className={'main_detail'}>
            <div className={'img_wrap'}>
                {Food.imageUrls  &&
                    <img src={Food.imageUrls[0]} alt={Food.title}/>
                }
            </div>
            <div className={'content_wrap'}>
                <div className={'user_wrap'}>
                    <Avatar className={'avatar'} circle/>
                    <div className={'user_info'}>
                        <p className={'nick_name'}>{Food.writer} 닉네임 자리입니다만</p>
                        <p className={'location'}>{Food.location}주소자리</p>
                    </div>
                </div>
                <div className={'title_wrap'}>
                    <h5>{Food.title}</h5>
                    <p className={'date'}>{Food.createdAt}1시간전</p>
                </div>
                <div className={'dates_wrap'}>
                    <p><span>소비기한</span>{Food.eatByDate}</p>
                </div>
                <div className={'description_wrap'}>
                    <p>{Food.description}</p>
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
                    <IconButton icon={faHeart}/>
                    <NavLink to={'/main'}>
                        <SquareButton name={'채팅하기'}/>
                    </NavLink>
                </div>
            </div>
        </section>
    )
}