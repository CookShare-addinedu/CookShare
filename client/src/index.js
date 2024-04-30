import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {BrowserRouter} from 'react-router-dom';
import {Provider} from "react-redux";
import store from "./redux/store";
import {isTokenValid} from "./views/Notification/utils/isTokenValid";
import SSEComponent from "./views/Notification/SSEComponent";

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);
const tokenIsValid = isTokenValid();

root.render(
    <Provider store={store}>
        <BrowserRouter>
            {tokenIsValid && <SSEComponent/>}
            <App/>
        </BrowserRouter>
    </Provider>
);
