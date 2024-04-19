import React, {useEffect, useState} from 'react';
import axios from 'axios';
import './FoodForm.css';
import {useLocation, useNavigate} from "react-router-dom";

const FoodForm = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const initialData = location.state?.food;

    const [foodData, setFoodData] = useState({
        category: '',
        makeByDate: '',
        eatByDate: '',
        status: '',
        title: '',
        description: ''
    });

    useEffect(() => {
        console.log(initialData);
        if (initialData) {
            setFoodData({
                category: initialData.category,
                makeByDate: initialData.makeByDate,
                eatByDate: initialData.eatByDate,
                status: initialData.status,
                title: initialData.title,
                description: initialData.description
            });
            setSelectedStatus(initialData.status); // 상태를 설정합니다.

            // 이미지 URL을 이미지 미리보기 배열에 추가합니다.
            // 가정: initialData.imageUrls는 이미지 URL 문자열 배열입니다.
            if (initialData.imageUrls) {
                setImages(initialData.imageUrls.map(url => ({ url })));
            }
        }
    }, [initialData]);

    const [images, setImages] = useState([]); // 이미지 파일 목록 상태 추가

    const handleChange = (e) => {
        setFoodData({ ...foodData, [e.target.name]: e.target.value });
    };

    const [selectedStatus, setSelectedStatus] = useState("");

    const handleStatusChange = (statusValue) => {
        setFoodData({ ...foodData, status: statusValue });
        setSelectedStatus(statusValue); // 버튼의 스타일을 변경하기 위해 선택된 상태를 설정합니다.
    };
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

    // 이미지 제거 핸들러를 추가합니다.
    const handleRemoveImage = (index) => {
        setImages(prevImages => prevImages.filter((_, i) => i !== index));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();

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
            navigate(`/`); // 성공적으로 처리 후 해당 음식 상세 페이지로 이동
        } catch (error) {
            console.error('Error submitting food data', error);
        }
    };


    return (
        <div className="food-form-container">
            <header className="form-header">
                <button className="back-button">{"<"}</button>
                <h1>등록하기</h1>
            </header>
            <form onSubmit={handleSubmit} className="food-form">
                <div className="form-group">
                    <label htmlFor="image" className="image-label">
                        이미지 ({images.length}/5) {/* 이미지 개수 업데이트 */}
                        <input type="file" id="image" onChange={handleImageChange} className="image-input" multiple/>
                    </label>
                    <div className="image-preview">
                        {images.map((image, index) => (
                            <div key={index}>
                                <img src={image.url} alt={`Preview ${index}`} />
                                {/* 이미지 제거 버튼을 추가합니다. */}
                                <button type="button" onClick={() => handleRemoveImage(index)}>
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="form-group">
                    <label htmlFor="category">카테고리</label>
                    <input name="category" id="category" value={foodData.category} onChange={handleChange}
                           placeholder="식자재"/>
                </div>

                <div className="form-group">
                    <label htmlFor="makeByDate">제조일</label>
                    <input name="makeByDate" type="date" id="makeByDate" value={foodData.makeByDate}
                           onChange={handleChange}/>
                </div>
                <div className="form-group">
                    <label htmlFor="eatByDate">소비기한</label>
                    <input name="eatByDate" type="date" id="eatByDate" value={foodData.eatByDate}
                           onChange={handleChange}/>
                </div>
                <div className="form-group status-type">
                    <label>거래방식</label>
                    <div className="status-buttons">
                        {/* 각 버튼의 className에 선택된 상태를 기반으로 조건부 스타일을 적용합니다. */}
                        <button type="button" className={`status-button ${selectedStatus === '나눔하기' ? 'selected' : ''}`}
                                onClick={() => handleStatusChange('나눔하기')}>나눔하기
                        </button>
                        <button type="button" className={`status-button ${selectedStatus === '교환하기' ? 'selected' : ''}`}
                                onClick={() => handleStatusChange('교환하기')}>교환하기
                        </button>
                        <button type="button" className={`status-button ${selectedStatus === '나눔받기' ? 'selected' : ''}`}
                                onClick={() => handleStatusChange('나눔받기')}>나눔받기
                        </button>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="title">제목</label>
                    <input name="title" id="title" value={foodData.title} onChange={handleChange}
                           placeholder="제목을 입력해주세요"/>
                </div>
                <div className="form-group">
                    <label htmlFor="description">내용</label>
                    <textarea name="description" id="description" value={foodData.description} onChange={handleChange}
                              placeholder="내용을 입력해주세요"/>
                </div>
                <div className="form-group">
                    <button type="submit" className="submit-button">등록하기</button>
                </div>
            </form>
        </div>
    );
};
export default FoodForm;
