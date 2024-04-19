import './style/style.scss';
import {Route, Routes, useLocation} from "react-router-dom";
import {useEffect} from "react";
import router from "./router";

function App() {

    const location = useLocation();
    // const isMobile = useMediaQuery({ query: '(max-width: 767px)' });
    useEffect(() => {
        getComponent(location,"header");
        getComponent(location,"footer");
    }, [location]);

    const getComponent = (location,type) => {
        for (let i = 0; i < router.routes.length; i++) {
            if (router.routes[i].path === location.pathname) {

                return type === "header" ? router.routes[i].header : router.routes[i].footer
            }
        }
        return type === "header" ? null : null
    }
    return (
        <>
            {/*<SplashScreen/>*/}
            {getComponent(location,"header")}
            <Routes>
                {router.routes.map((route, index) => (
                    <Route path={route.path} element={route.component} key={index}/>
                ))}
            </Routes>
            {getComponent(location,"footer")}
        </>
    )
}

export default App;