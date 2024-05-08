import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import {Provider} from "react-redux";
import store from "./redux/store";
import {isTokenValid} from "./views/Notification/utils/isTokenValid";
import SSEComponent from "./views/Notification/SSEComponent";
import axios from "axios";

// Axios 인터셉터 설정
axios.interceptors.request.use(
    config => {
        // 로그인 API 요청에서는 Authorization 헤더를 추가하지 않음
        if (!config.url.endsWith('/api/user/login')) {
            const token = localStorage.getItem('jwt');
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);
const tokenIsValid = isTokenValid();

root.render(

        <Provider store={store}>
            {/*React.StrictMode 지우세요*/}
            <BrowserRouter>
                {tokenIsValid && <SSEComponent />}
                <App />
            </BrowserRouter>
        </Provider>

);
