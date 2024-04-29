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


const MainForm = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const swiperRef = useRef(null);
    const initialData = location.state?.food;
    const [images, setImages] = useState([]); // 이미지 파일 목록 상태 추가
    const [foodData, setFoodData] = useState({
        category:'한식',
        makeByDate: '',
        eatByDate: '',
        status: '',
        title: '',
        description: '',
        location:''
    });

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
        console.log('Updated foodData:', foodData);
    }, [foodData]);

    useEffect(() => {
        console.log(initialData);
        if (initialData) {
            setFoodData({
                category: initialData.category,
                makeByDate: initialData.makeByDate,
                eatByDate: initialData.eatByDate,
                status: initialData.status,
                title: initialData.title,
                description: initialData.description,
                location:initialData.location
            });
            setSelectedStatus(initialData.status); // 상태를 설정합니다.

            // 이미지 URL을 이미지 미리보기 배열에 추가합니다.
            // 가정: initialData.imageUrls는 이미지 URL 문자열 배열입니다.
            if (initialData.imageUrls) {
                setImages(initialData.imageUrls.map(url => ({ url })));
            }
        }
    }, [initialData]);

    const handleChange = (e) => {
        setFoodData({ ...foodData, [e.target.name]: e.target.value });
    };

    const [selectedStatus, setSelectedStatus] = useState("");

    // const handleStatusChange = (statusValue) => {
    //     setFoodData({ ...foodData, status: statusValue });
    //     setSelectedStatus(statusValue); // 버튼의 스타일을 변경하기 위해 선택된 상태를 설정합니다.
    // };
    //
    // 이미지 변경 핸들러를 수정합니다.
    const handleImageChange = (e) => {
        if (e.target.files) {
            // 로컬에서 선택된 이미지 파일들의 배열을 생성합니다.
            const fileImages = Array.from(e.target.files).map(file => ({
                file,
                url: URL.createObjectURL(file)
            }));
            // 기존 이미지와 새로운 이미지를 결합합니다.
            setImages(prevImages => [...prevImages, ...fileImages].slice(0, 5));
        }
    };

    const handleMakeByDateChange = (dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        setFoodData(prevData => ({
            ...prevData,
            makeByDate: formattedDate
        }));
    };

    const handleEatByDateChange = (dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        setFoodData(prevData => ({
            ...prevData,
            eatByDate: formattedDate
        }));
    };

    const handleLocationSelect = (selectedLocation) => {
        setFoodData(prevVal => ({
            ...prevVal,
            location: selectedLocation
        }));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();

        formData.append('makeByDate', foodData.makeByDate);
        formData.append('eatByDate', foodData.eatByDate);

        if (images.length > 0) {
            images.forEach(image => {
                if (image.file) { // 'image.file'이 존재하는 경우만 추가
                    formData.append('images', image.file);
                }
            });
        }

        Object.keys(foodData).forEach(key => {
            formData.append(key, foodData[key]);
        });

        try {
            let response;
            if (initialData?.foodId) {
                // 데이터 수정 (PUT 요청)
                console.log("수정 요청들어옴");
                response = await axios.put(`/api/foods/${initialData.foodId}`, formData);
            } else {
                // 새 데이터 추가 (POST 요청)
                console.log(initialData);
                console.log("추가 요청들어옴");
                response = await axios.post('/api/foods', formData);
            }
            console.log(response.data);
            navigate(`/main`); // 성공적으로 처리 후 해당 음식 상세 페이지로 이동
        } catch (error) {
            console.error('Error submitting food data', error);
        }
    };

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
                            value={foodData.makeByDate ? new Date(foodData.makeByDate) : null}
                            onChange={handleMakeByDateChange}
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
                            value={foodData.eatByDate ? new Date(foodData.eatByDate) : null}
                            onChange={handleEatByDateChange}
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
                        value={foodData.title}
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
                        value={foodData.description}
                        onChange={handleChange}
                        placeholder="내용을 입력해주세요"
                    />
                </div>

                <div className="form_group">
                    <Select
                        name="category"
                        id="category"
                        value={foodData.category}
                        onChange={(value) => {
                            handleChange({
                                target: {
                                    name: "category",
                                    value: value
                                }
                            });
                        }}
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
                                    value={foodData.location}
                                    placeholder="원하는 장소를 입력해주세요"
                                    readOnly
                                />
                                <FontAwesomeIcon className={'arrow'} icon={faAngleRight}/>
                            </div>

                        }
                        drawerContent={<Address onLocationSelect={handleLocationSelect}/>}
                        onLocationSelect={handleLocationSelect}
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