import {createSlice} from "@reduxjs/toolkit";

export const shareSlice = createSlice({
    name: 'share',
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
            createdAt: '',
            price: '',
            maxTo: '',
            giver: {
                nickName: ''
            }
        }
    },
    reducers: {
        setShare: (state, action) => {
            state.value = action.payload;
        }
    }
})
export const {setShare} = shareSlice.actions

export default shareSlice.reducer