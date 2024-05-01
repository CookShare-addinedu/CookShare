import './MainDetail.scss';
import {useEffect} from "react";
import {NavLink,useParams} from "react-router-dom";
import axios from "axios";
import { Avatar } from "rsuite";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import {faAngleRight} from "@fortawesome/free-solid-svg-icons";
import CautionData from "../../../data/CautionData";
import {IconButton, SquareButton} from "../../../components/button/Button";
import Caution from "../../../components/caution/Caution";
import MapView from "../../../components/adress/MapView";
import {useDispatch, useSelector} from "react-redux";
import {clearFood, setFood} from "../../../redux/foodSlice";

export default function MainDetail() {
    const {id} = useParams();
    const dispatch = useDispatch();
    const food = useSelector((state) => state.food.value);
    // const [food, setFood] = useState({});
    useEffect(() => {
        if(!food){
            const fetchFoodsData = async () => {
                console.log('푸드 아이디:', id);
                try {
                    const response = await axios.get(`/api/foods/${id}`);
                    dispatch(setFood(response.data));
                    console.log('API로부터 받은 데이터:', response.data); // 데이터 수신 후
                } catch (error) {
                    console.error('Failed to fetch data:', error);
                    dispatch(clearFood());
                }
            };
            fetchFoodsData();
            console.log('푸드데이터:', food);
        }
        // 컴포넌트 언마운트 시 데이터 초기화
        return () => {
            dispatch(clearFood());
        };
    }, [id, dispatch]);

    if (!food) {
        return <div>Loading...</div>;
    }

    // useEffect(() => {
    //     if(!food){
    //         fetchFoodsData(id);
    //     }
    // },[id, food, dispatch])


    // const fullScreenMapStyle = {
    //     width: '100vw',
    //     height: '100vh',
    //     position: 'fixed',
    //     top: 0,
    //     left: 0,
    //     zIndex: 1050 // Drawer z-index보다 높거나 같게 설정
    // };

    // useEffect(() => {
    //     fetchFoodsData();
    //     return () => {
    //         dispatch(clearFood());
    //     };
    // }, [dispatch, id]);

    // const fetchFoodsData = async () => {
    //     console.log('푸드 아이디:', id);
    //     try{
    //         const response = await axios.get(`/api/foods/${id}`);
    //         console.log('받아온 데이터 로그에 출력:', response.data);
    //         dispatch(setFood(response.data));
    //     }catch (error){
    //         console.log('Falied to fetch data:', error);
    //         dispatch(clearFood());
    //     }
    // };
    // fetchFoodsData();


    // if (!food) {
    //     return <div>Loading...</div>; // 데이터가 없거나 로딩 중일 때 표시할 메시지
    // }

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
                    <IconButton icon={faHeart}/>
                    <NavLink to={'/main'}>
                        <SquareButton name={'채팅하기'}/>
                    </NavLink>
                </div>
            </div>
        </section>
    )
}