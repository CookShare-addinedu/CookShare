import './style/style.scss';
import {useMediaQuery} from "react-responsive";
import {Route, Routes} from "react-router-dom";
import Login from "./views/auth/login/Login";
import Register from "./views/auth/register/Register";
import Board from "./views/pages/board/Board";
function App() {
    const isMobile = useMediaQuery({ query: '(max-width: 767px)' });
    return (
        <>
            {isMobile ? <h1>푸드쉐어Mobile</h1> : <h1>푸드쉐어Desktop</h1>}
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/" element={<Board />} />
            </Routes>
        </>
  );
}

export default App;

