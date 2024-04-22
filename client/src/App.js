import './style/style.scss';
import {Route, Routes, useLocation} from "react-router-dom";
import {useEffect} from "react";
import router from "./router";
import MainDetail from "./views/pages/main/MainDetail";
import Main from "./views/pages/main/Main";
import MainForm from "./views/pages/main/MainForm";
function App() {

    const location = useLocation();
    console.log("3000 포트로 들어왔어 app.js 실행하자")
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
            <main>
                {getComponent(location,"header")}
                <Routes>
                    {/*<Route path="/" element={<Main />} />*/}
                    <Route path="/foods/:id" element={<MainDetail />} />
                    <Route path="/add" element={<MainForm />} />
                    <Route path="/edit-food/:id" element={<MainForm />} />

                    {router.routes.map((route, index) => (
                        <Route path={route.path} element={route.component} key={index}/>
                    ))}
                </Routes>
                {getComponent(location,"footer")}
            </main>
        </>
    )
}

export default App;

