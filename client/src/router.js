import Login from "./views/auth/login/Login";
import {Header1, Header2, Header3, Header4, Header5} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";
const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            header:<Header2/>,
            footer:<Footer/>
        },
    ]
}
export default router;