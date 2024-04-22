import Login from "./views/auth/login/Login";
import {Header1, Header2, Header3, Header4, Header5} from "./views/common/header/Header";
import Footer from "./views/common/footer/Footer";
import OnBoarding from "./views/pages/onboarding/OnBoarding";
const router = {
    routes: [
        {
            path: '/login',
            component: <Login/>,
            header:<Header2/>,
            footer:null
        },
        {
            path: '/',
            component: <OnBoarding/>,
            header:null,
            footer:null
        }
    ]
}
export default router;