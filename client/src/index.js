import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import {Provider} from "react-redux";
import store from "./redux/store";
import axios from "axios";
const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);

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

root.render(
    <React.StrictMode>
        <Provider store={store}>
            <BrowserRouter>
                <App />
            </BrowserRouter>
        </Provider>
    </React.StrictMode>
);
