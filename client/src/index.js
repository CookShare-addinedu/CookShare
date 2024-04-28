import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {BrowserRouter} from 'react-router-dom';
import {Provider} from "react-redux";

const rootElement = document.getElementById('root');
const root = ReactDOM.createRoot(rootElement);


root.render(



            <BrowserRouter>
                <App />
            </BrowserRouter>


);
