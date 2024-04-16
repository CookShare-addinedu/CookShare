import './style/style.scss';
import {useMediaQuery} from "react-responsive";
import {Route, Routes} from "react-router-dom";
import Login from "./views/auth/login/Login";
import Register from "./views/auth/register/Register";
import Footer from "./views/common/footer/Footer";
import {Header1, Header2, Header3, Header4} from "./views/common/header/Header";
import SplashScreen from "./views/pages/SplashScreen";


function App() {
    const isMobile = useMediaQuery({ query: '(max-width: 767px)' });
    return (
        <>
            {isMobile ?  <Header1/> : <h1>푸드쉐어Desktop</h1>}
            <Routes>
                <Route path="/" element={<SplashScreen/>} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/" element={<Board />} />
            </Routes>
            <Footer/>
        </>
  );
}

export default App;

