import React, { useState } from 'react';
import axios from 'axios';
import './FoodForm.css';

const FoodForm = () => {
    const [foodData, setFoodData] = useState({
        image: '', // 이미지 파일 상태 추가
        category: '', // 카테고리 ID 또는 이름을 입력받을 것
        makeByDate: '',
        eatByDate: '',
        status: '',
        title: '',
        description: ''
    });

    const handleChange = (e) => {
        setFoodData({ ...foodData, [e.target.name]: e.target.value });
    };

    const [selectedStatus, setSelectedStatus] = useState("");

    const handleStatusChange = (statusValue) => {
        setFoodData({ ...foodData, status: statusValue });
        setSelectedStatus(statusValue); // 버튼의 스타일을 변경하기 위해 선택된 상태를 설정합니다.
    };
    const handleImageChange = (e) => {
        setFoodData({ ...foodData, image: e.target.files[0] });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('image', foodData.image);
        Object.keys(foodData).forEach(key => {
            if (key !== 'image') { // image 키를 제외하고 formData에 추가
                formData.append(key, foodData[key]);
            }
        });

        try {
            // 서버에 POST 요청
            console.log(formData.get('image'));
            console.log(formData.get('title'));
            console.log(foodData);
            const response = await axios.post('/api/foods', formData);
            console.log(response.data);
            // 처리 완료 후 추가 동작(예: 폼 초기화, 알림 표시 등)
        } catch (error) {
            console.error('Error posting food data', error);
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
                        이미지 (0/5)
                        <input type="file" id="image" onChange={handleImageChange} className="image-input"/>
                    </label>
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
