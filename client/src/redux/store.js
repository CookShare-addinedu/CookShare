import {configureStore} from "@reduxjs/toolkit";
import addressReducer from "./addressSlice";
import foodReducer from "./foodSlice";

export const store = configureStore({
    reducer: {
        address: addressReducer,
        food: foodReducer,
    },
})
export default store;