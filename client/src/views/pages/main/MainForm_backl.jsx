import React, {useEffect, useRef, useState} from 'react';
import axios from 'axios';
import {useLocation, useNavigate} from "react-router-dom";
import './MainForm.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleRight, faCamera, faXmark} from "@fortawesome/free-solid-svg-icons";
import {Swiper, SwiperSlide} from "swiper/react";
import {faCircleXmark} from "@fortawesome/free-solid-svg-icons/faCircleXmark";
import {DatePicker} from "rsuite";
import {FaCalendarDay} from 'react-icons/fa';
import {format} from "date-fns";
import {SquareButton} from "../../../components/button/Button";
import Select from "../../../components/select/Select";
import Drawers from "../../../components/drawer/Drawers";
import Address from "../../../components/adress/Address";
import {useDispatch, useSelector} from "react-redux";
import {clearFood, setFood} from "../../../redux/foodSlice";


const MainForm = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const editLocation = useLocation();
    const food = useSelector((state) => state.food.value);
    const swiperRef = useRef(null);
    const initialData = editLocation.state?.food;
    const [images, setImages] = useState([]);
    const [localFood, setLocalFood] = useState([]);
    // const [localFood, setLocalFood] = useState(location.state?.food || food || {});
    // const [images, setImages] = useState(food.imageUrls || []); // 이미지 파일 목록 상태 추가
    // const [images, setImages] = useState(food ? (food.imageUrls || []) : []);
    // const [food, setFood] = useState({
    //     category:'한식',
    //     makeByDate: '',
    //     eatByDate: '',
    //     status: '',
    //     title: '',
    //     description: '',
    //     location:''
    // });

    useEffect(() => {
        // food 객체가 유효할 때만 실행
        if (food) {
            setLocalFood(food);

            // 이미지 URL이 존재하면 설정, 그렇지 않으면 빈 배열 설정
            setImages(food.imageUrls ? food.imageUrls.map(url => ({ url })) : []);
        }
    }, [food])

    const handleChange = (e) => {
        const {name, value} = e.target;
        setLocalFood(prev => ({
            ...prev,
            [name]: value
        }));
    }

    // 이미지 변경 핸들러를 수정합니다.
    const handleImageChange = (e) => {
        if (e.target.files) {
            // 로컬에서 선택된 이미지 파일들의 배열을 생성합니다.
            const fileImages = Array.from(e.target.files).map(file => ({
                file,
                url: URL.createObjectURL(file)
            }));
            // 기존 이미지와 새로운 이미지를 결합합니다.
            setImages([...images, ...fileImages]);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!food) {
            console.error('No food data available');
            return; // 또는 사용자에게 오류 메시지를 표시
        }

        const formData = new FormData();
        formData.append('makeByDate', localFood.makeByDate);
        formData.append('eatByDate', localFood.eatByDate);

        // dispatch(setFood(localFood));

        if (images.length > 0) {
            images.forEach(image => {
                if (image.file) { // 'image.file'이 존재하는 경우만 추가
                    formData.append('images', image.file);
                }
            });
        }

        Object.keys(localFood).forEach(key => {
            formData.append(key, localFood[key]);
        });

        try {
            let response;
            if (localFood?.id) {
                // 데이터 수정 (PUT 요청)
                console.log("수정 요청들어옴");
                response = await axios.put(`/api/foods/${localFood.id}`, formData);
            } else {
                // 새 데이터 추가 (POST 요청)
                console.log(localFood);
                console.log("추가 요청들어옴");
                response = await axios.post('/api/foods', formData);
            }
            console.log(response.data);
            dispatch(clearFood());
            navigate(`/main`); // 성공적으로 처리 후 해당 음식 상세 페이지로 이동
        } catch (error) {
            console.error('Error submitting food data', error);
        }
    };

    useEffect(() => {
        // location.state.food가 있으면 그것을 사용하고, 없으면 Redux에서 가져온 값 사용
        if (initialData) {
            setLocalFood(editLocation.state.food);
            setImages(editLocation.state.food.imageUrls.map(url => ({ url, file: null })));
        } else if (food) {
            setLocalFood(food);
            setImages(food.imageUrls.map(url => ({ url, file: null })));
        }
    }, [editLocation.state, food]);

    // // 이미지 변경 핸들러를 수정합니다.
    // const handleImageChange = (e) => {
    //     if (e.target.files) {
    //         // 로컬에서 선택된 이미지 파일들의 배열을 생성합니다.
    //         const fileImages = Array.from(e.target.files).map(file => ({
    //             file,
    //             url: URL.createObjectURL(file)
    //         }));
    //         // 기존 이미지와 새로운 이미지를 결합합니다.
    //         setImages(prevImages => [...prevImages, ...fileImages].slice(0, 5));
    //     }
    // };

    // useEffect(() => {
    //     if(initialData){
    //         setLocalFood(editLocation.state.food);
    //     }
    // },[initialData])

    // useEffect(()=>{
    //     return() => dispatch(clearFood());
    // },[dispatch]);

    // const handleChange = (e) => {
    //     dispatch(setFood({ ...food, [e.target.name]: e.target.value }));
    // };



    const handleRemoveImage = (index) => {
        setImages(prevImages => {
            const newImages = prevImages.filter((_, i) => i !== index);
            if (swiperRef.current && swiperRef.current.swiper) {
                swiperRef.current.swiper.update();
            }
            return newImages;
        });
    };

    useEffect(() => {
        console.log('Updated food:', food);
    }, [food]);

  /*  useEffect(() => {
        console.log(initialData);
        if (initialData) {
            setFood(initialData)
            // setFood({
            //     category: initialData.category,
            //     makeByDate: initialData.makeByDate ? new Date(initialData.makeByDate) : '',
            //     eatByDate: initialData.eatByDate ? new Date(initialData.eatByDate) : '',
            //     status: initialData.status,
            //     title: initialData.title,
            //     description: initialData.description,
            //     location:initialData.location,

            // });
            // setSelectedStatus(initialData.status); // 상태를 설정합니다.

            // 이미지 URL을 이미지 미리보기 배열에 추가합니다.
            // 가정: initialData.imageUrls는 이미지 URL 문자열 배열입니다.
            if (initialData.imageUrls) {
                setImages(initialData.imageUrls.map(url => ({ url })));
            }
        }
    }, [initialData]);*/

    const handleMakeByDateChange = (dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        setFood(prevData => ({
            ...prevData,
            makeByDate: formattedDate
        }));
    };

    const [selectedStatus, setSelectedStatus] = useState("");

    // const handleStatusChange = (statusValue) => {
    //     setFood({ ...foodData, status: statusValue });
    //     setSelectedStatus(statusValue); // 버튼의 스타일을 변경하기 위해 선택된 상태를 설정합니다.
    // };
    //




    const handleEatByDateChange = (dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        setFood(prevData => ({
            ...prevData,
            eatByDate: formattedDate
        }));
    };

    const handleLocationSelect = (selectedLocation) => {
        setFood(prevVal => ({
            ...prevVal,
            location: selectedLocation
        }));
    }

    // const handleSubmit = async (e) => {
    //     e.preventDefault();
    //     const formData = new FormData();
    //
    //     formData.append('makeByDate', food.makeByDate);
    //     formData.append('eatByDate', food.eatByDate);
    //
    //     dispatch(setFood(localFood));
    //
    //     if (images.length > 0) {
    //         images.forEach(image => {
    //             if (image.file) { // 'image.file'이 존재하는 경우만 추가
    //                 formData.append('images', image.file);
    //             }
    //         });
    //     }
    //
    //     Object.keys(food).forEach(key => {
    //         formData.append(key, food[key]);
    //     });
    //
    //     try {
    //         let response;
    //         if (food?.id) {
    //             // 데이터 수정 (PUT 요청)
    //             console.log("수정 요청들어옴");
    //             response = await axios.put(`/api/foods/${food.id}`, formData);
    //         } else {
    //             // 새 데이터 추가 (POST 요청)
    //             console.log(food);
    //             console.log("추가 요청들어옴");
    //             response = await axios.post('/api/foods', formData);
    //         }
    //         console.log(response.data);
    //         dispatch(clearFood());
    //         navigate(`/main`); // 성공적으로 처리 후 해당 음식 상세 페이지로 이동
    //     } catch (error) {
    //         console.error('Error submitting food data', error);
    //     }
    // };

    return (
        <div className="form_wrap">
            <form onSubmit={handleSubmit}>
                <div className="form_group">
                    <div className={"image_wrap"}>
                        <label htmlFor="image">
                            <span className="camera_icon">
                                <FontAwesomeIcon icon={faCamera}/>
                            </span>
                            <span className={'txt'}>이미지 ({images.length}/5)</span>
                            <input
                                className="image_input"
                                type="file"
                                id="image"
                                onChange={handleImageChange}
                                multiple
                            />
                        </label>
                    </div>
                    <Swiper
                        ref={swiperRef}
                        className="preview_swiper"
                        slidesPerView={'auto'}
                        spaceBetween={10}
                        centeredSlides={false}
                    >
                        {images.map((image, index) => (
                            <SwiperSlide key={index}>
                                <div>
                                    <img src={image.url} alt={`Preview ${index}`}/>
                                    {/* 이미지 제거 버튼을 추가합니다. */}
                                    <button className={'close'} type="button" onClick={() => handleRemoveImage(index)}>
                                        <FontAwesomeIcon icon={faCircleXmark}/>
                                    </button>
                                </div>
                            </SwiperSlide>
                        ))}
                    </Swiper>
                </div>

                <div className="form_group dates_wrap">
                    <div className={'date'}>
                        <p>제조일</p>
                        <DatePicker
                            className={'date_picker'}
                            caretAs={FaCalendarDay}
                            oneTap
                            name="makeByDate"
                            id="makeByDate"
                            // value={food.makeByDate ? new Date(localFood.makeByDate) : null}
                            // onChange={handleMakeByDateChange}
                            value={new Date(localFood.makeByDate)}
                            onChange={(dateValue) => setLocalFood({...localFood, makeByDate: dateValue})}
                        />
                    </div>

                    <div className={'date'}>
                        <p>소비기한</p>
                        <DatePicker
                            className={'date_picker'}
                            caretAs={FaCalendarDay}
                            oneTap
                            name="eatByDate"
                            id="eatByDate"
                            // value={food.eatByDate ? new Date(localFood.eatByDate) : null}
                            // onChange={handleEatByDateChange}
                            value={new Date(localFood.eatByDate)}
                            onChange={(dateValue) => setLocalFood({...localFood, eatByDate: dateValue})}
                        />
                    </div>
                </div>
                {/*<div className="form_group status_type">*/}
                {/*    <label>거래방식</label>*/}
                {/*    <div className="status-buttons">*/}
                {/*        /!* 각 버튼의 className에 선택된 상태를 기반으로 조건부 스타일을 적용합니다. *!/*/}
                {/*        <button type="button" className={`status-button ${selectedStatus === '나눔하기' ? 'selected' : ''}`}*/}
                {/*                onClick={() => handleStatusChange('나눔하기')}>나눔하기*/}
                {/*        </button>*/}
                {/*        <button type="button" className={`status-button ${selectedStatus === '교환하기' ? 'selected' : ''}`}*/}
                {/*                onClick={() => handleStatusChange('교환하기')}>교환하기*/}
                {/*        </button>*/}
                {/*    </div>*/}
                {/*</div>*/}
                <div className="form_group">
                    <label htmlFor="title" className={'a11y-hidden'}>제목</label>
                    <input
                        className={'title'}
                        name="title"
                        id="title"
                        value={localFood.title}
                        onChange={handleChange}
                        placeholder="글 제목"
                    />
                </div>
                <div className="form_group">
                    <label htmlFor="description" className={'a11y-hidden'}>내용</label>
                    <textarea
                        className={'description'}
                        name="description"
                        id="description"
                        value={localFood.description}
                        onChange={handleChange}
                        placeholder="내용을 입력해주세요"
                    />
                </div>

                <div className="form_group">
                    <Select
                        name="category"
                        id="category"
                        value={localFood.category}
                        // onChange={(value) => {
                        //     handleChange({
                        //         target: {
                        //             name: "category",
                        //             value: value
                        //         }
                        //     });
                        // }}
                        onChange={(value) => setLocalFood({...localFood, category: value})}
                    />
                </div>
                <div className={'form_group'}>
                    <Drawers
                        trigger={
                            <div className={'location_wrap'}>
                                <input
                                    className={'location'}
                                    type="text"
                                    id={'location'}
                                    name={'location'}
                                    value={localFood.location}
                                    placeholder="원하는 장소를 입력해주세요"
                                    readOnly
                                />
                                <FontAwesomeIcon className={'arrow'} icon={faAngleRight}/>
                            </div>

                        }
                        // drawerContent={<Address onLocationSelect={handleLocationSelect}/>}
                        // onLocationSelect={handleLocationSelect}
                        drawerContent={<Address onLocationSelect={(selectedLocation) => setLocalFood({...localFood, location: selectedLocation})}/>}
                    />

                </div>

                <div className="form_group btn_wrap">
                    <SquareButton className={'submit_button'} name={'등록하기'}/>
                </div>
            </form>
        </div>
    );
};
export default MainForm;