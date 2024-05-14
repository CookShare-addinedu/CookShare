import { createSlice } from '@reduxjs/toolkit';
export const foodSlice = createSlice({
    name: 'food',
    initialState: {
        value: {
            title: '',
            description: '',
            location: '',
            locationDetails: {
                name: '',
                lat: 37.5665,
                lng: 126.9780
            },
            makeByDate: '',
            eatByDate: '',
            createdAt: '',
            category: '한식',
            images: [],
            giver:{
                nickName: '',
            }
        }
    },
    reducers: {
        setFood: (state, action) => {
            state.value = action.payload;
        },
        setLocationDetails: (state, action) => {
            state.value.locationDetails = action.payload;
        },
        addImage: (state, action) => {
            state.value.images.push({
                file: action.payload.file,
                url: action.payload.url || URL.createObjectURL(action.payload.file)
            });
        },
        removeImage: (state, action) =>{
            state.value.images = state.value.images.filter((_, index) => index !== action.payload);
        },
        clearFood: (state) => {
            state.value = {
                title: '',
                description: '',
                location: '',
                locationDetails: {
                    name: '',
                    lat: 37.5665,
                    lng: 126.9780
                },
                makeByDate: '',
                eatByDate: '',
                createdAt: '',
                category: '',
                images: [],
                giver:{
                    nickName: '',
                }
            };
        },
    },
});

export const { setFood, clearFood, addImage, removeImage,setLocationDetails } = foodSlice.actions;

export default foodSlice.reducer;
