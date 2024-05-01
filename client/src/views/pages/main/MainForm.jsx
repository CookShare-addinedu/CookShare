import React, {useCallback, useEffect, useRef, useState} from 'react';
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
import {addImage, clearFood, removeImage, setFood} from "../../../redux/foodSlice";
import {isEqual} from "lodash";


const MainForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const { state }  = useLocation();
    const swiperRef = useRef(null);
    const foodData = useSelector(state => state.food.value)
    const initialData = state?.foodData || foodData;
    const images = useSelector(state => state.food.value.images || []);
    // const [images, setImages] = useState([]);

    useEffect(() => {
        if (initialData) {
            dispatch(setFood(initialData));
        }

        const newFoodData = {
            ...initialData,
            images: initialData.imageUrls ? initialData.imageUrls.map(url => ({url, file: null})) : []
        };

        dispatch(setFood(newFoodData));
    },[dispatch]);

//     console.log("초기데이터 확인:", initialData);
//     if (!isEqual(foodData, initialData)) {
//         if (initialData?.imageUrls) {
//             const newFoodData = {
//                 ...initialData,
//                 images: initialData.imageUrls.map(url => ({url, file: null})),
//             }
//             dispatch(setFood(newFoodData));
//         } else {
//             dispatch(setFood(initialData));
//         }
//     }
// }, [dispatch, initialData, foodData]);

    const handleChange = useCallback((e) => {
        const {name, value} = e.target;
        dispatch(setFood({...foodData, [name]: value}));
    },[dispatch, foodData]);
    const handleImageChange = (e) => {
        if (e.target.files) {
            const fileImages = Array.from(e.target.files).map(file => ({
                file,
                url: URL.createObjectURL(file),
            }));
            fileImages.forEach(image =>{
                dispatch(addImage(image));
            });
        }
    };
    const handleRemoveImage = (index) => {
        dispatch(removeImage(index));
        if(swiperRef.current && swiperRef.current.swiper) {
            swiperRef.current.swiper.update();
        }
    };

    //     setImages(prevImages => {
    // const handleRemoveImage = (index) => {
    //         const newImages = prevImages.filter((_, i) => i !== index);
    //         if (swiperRef.current && swiperRef.current.swiper) {
    //             swiperRef.current.swiper.update();
    //         }
    //         return newImages;
    //     });
    // };
    const handleDateChange = (name, dateValue) => {
        const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
        dispatch(setFood({ ...foodData, [name]: formattedDate }));
    };

    const handleLocationSelect = (selectedLocation) => {
        dispatch(setFood({...foodData, location: selectedLocation}));
    }


    //     if (initialData) {
    //         const completeData = {
    //             title: initialData.title || '',
    //             description: initialData.description || '',
    //             location: initialData.location || '',
    //             makeByDate: initialData.makeByDate || '',
    //             eatByDate: initialData.eatByDate || '',
    //             category: initialData.category || '',
    //             images: initialData.images || []
    //         };
    //         console.log("완성된 데이터:", completeData);
    //         dispatch(setFood(completeData));
    //         setImages(initialData.images?.map(url => ({ url })) || []);
    //     }else{
    //         console.log("초기 데이터가 정의되지 않았습니다");
    //         dispatch(clearFood()); // 데이터가 없는 경우 명시적으로 상태를 초기화
    //     }
    // }, [initialData, dispatch]);

    useEffect(() => {
        console.log('Redux로부터 업데이트된 food data:', foodData);
    }, [foodData]); // foodData가 변경될 때마다 실행

    // useEffect(() =>{
    //     if(!foodData && initialData){
    //         dispatch(setFood(initialData));
    //         if(initialData.imageUrls){
    //             setImages(initialData.imageUrls.map(url => url));
    //         }
    //
    //     }
    // },[dispatch, initialData, foodData]);







    // const handleDateChange = (name, dateValue) =>{
    //     const formattedDate = dateValue? format(dateValue, 'yyyy-MM-dd') : '';
    //     dispatch(setFood({...foodData, [name]: formattedDate}));
    // }



    const handleSubmit = async (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('title', foodData.title);
        formData.append('description', foodData.description);
        formData.append('makeByDate', format(foodData.makeByDate, 'yyyy-MM-dd'));
        formData.append('eatByDate', format(foodData.eatByDate, 'yyyy-MM-dd'));
        formData.append('category', foodData.category);
        formData.append('location', foodData.location);
        // 이미지 파일 체크 및 추가
        foodData.images.forEach((image, index) => {
            if (image.file) { // 파일이 실제로 존재하는지 확인
                formData.append(`images[${index}]`, image.file);
                console.log(`Image added to formData: images[${index}]`, image.file);
            }
        });

        console.log("FormData 내용 검사:");
        for (let [key, value] of formData.entries()) {
            console.log(`${key}:`, value);
        }



        const method = foodData.foodId ? 'PUT' : 'POST';
        const url = foodData.foodId ? `/api/foods/${foodData.foodId}` : '/api/foods';
        const token = localStorage.getItem('jwt');

        try {
            const response = await axios({
                method: method,
                url: url,
                data: formData,
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            console.log('서버 응답', response.data);
            navigate('/main');
        } catch (error) {
            console.error('Error submitting food data:', error);
            alert('제출 중 오류가 발생했습니다: ' + (error.response?.data?.message || error.message));
        }
    };










    //     let response;
    //     if (initialData?.foodId) {
    //         // 데이터 수정 (PUT 요청)
    //         console.log("수정 요청들어옴");
    //         response = await axios.put(`/api/foods/${initialData.foodId}`, formData);
    //     } else {
    //         // 새 데이터 추가 (POST 요청)
    //         console.log(initialData);
    //         console.log("추가 요청들어옴");
    //         response = await axios.post('/api/foods', formData);
    //     }
    //     console.log(response.data);
    //     navigate(`/main`); // 성공적으로 처리 후 해당 음식 상세 페이지로 이동
    // } catch (error) {
    //     console.error('Error submitting food data', error);
    // }



    // useEffect(() => {
    //     console.log('Updated food:', foodData);
    // }, [foodData]);

    // useEffect(() => {
    //     console.log(initialData);
    //     if (initialData) {
    //         setFood({
    //             category: initialData.category,
    //             makeByDate: initialData.makeByDate,
    //             eatByDate: initialData.eatByDate,
    //             status: initialData.status,
    //             title: initialData.title,
    //             description: initialData.description,
    //             location:initialData.location
    //         });
    //         setSelectedStatus(initialData.status); // 상태를 설정합니다.
    //
    //         // 이미지 URL을 이미지 미리보기 배열에 추가합니다.
    //         // 가정: initialData.imageUrls는 이미지 URL 문자열 배열입니다.
    //         if (initialData.imageUrls) {
    //             setImages(initialData.imageUrls.map(url => ({ url })));
    //         }
    //     }
    // }, [initialData]);



    // const [selectedStatus, setSelectedStatus] = useState("");



    // const handleMakeByDateChange = (dateValue) => {
    //     const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
    //     setFood(prevData => ({
    //         ...prevData,
    //         makeByDate: formattedDate
    //     }));
    // };
    //
    // const handleEatByDateChange = (dateValue) => {
    //     const formattedDate = dateValue ? format(dateValue, 'yyyy-MM-dd') : '';
    //     setFood(prevData => ({
    //         ...prevData,
    //         eatByDate: formattedDate
    //     }));
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
                        {images && images.map((image, index) => (
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
                            // value={food.makeByDate ? new Date(food.makeByDate) : null}
                            // value={new Date(foodData.makeByDate)}
                            value={foodData.makeByDate ? new Date(foodData.makeByDate) : null}
                            onChange={value => handleDateChange('makeByDate', value)}
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
                            // value={new Date(foodData.eatByDate)}

                            onChange={value => handleDateChange('eatByDate', value)}
                        />
                    </div>
                </div>

                <div className="form_group">
                    <label htmlFor="title" className={'a11y-hidden'}>제목</label>
                    <input
                        className={'title'}
                        name="title"
                        id="title"
                        value={foodData.title || ''}
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
                        value={foodData.description || ''}
                        onChange={handleChange}
                        placeholder="내용을 입력해주세요"
                    />
                </div>

                <div className="form_group">
                    <Select
                        name="category"
                        id="category"
                        value={foodData.category || ''}
                        debounce={300}
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