import { createSlice } from '@reduxjs/toolkit';

export const foodSlice = createSlice({
    name: 'food',
    initialState: {
        value: {
            title: '',
            description: '',
            location: '',
            makeByDate: '',
            eatByDate: '',
            category: '',
            images: [],
        }
    },
    reducers: {
        setFood: (state, action) => {
            state.value = action.payload;
        },
        clearFood: (state) => {
            state.value = {
                title: '',
                description: '',
                location: '',
                makeByDate: '',
                eatByDate: '',
                category: '',
                images: []
            };
        },
    },
});

export const { setFood, clearFood } = foodSlice.actions;

export default foodSlice.reducer;
