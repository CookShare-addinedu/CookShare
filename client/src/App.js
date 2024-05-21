import './style/style.scss';
import {matchPath, Route, Routes, useLocation} from "react-router-dom";
import {useEffect, useState} from "react";
import router from "./router";
function App() {
    const location = useLocation();
    const [currentHeader, setCurrentHeader] = useState(null);
    const [currentFooter, setCurrentFooter] = useState(null);

    function matchCustomPath(routePath, currentPath) {
        const routeParts = routePath.split('/');
        const currentParts = currentPath.split('/');
        if (routeParts.length !== currentParts.length) return false;

        return routeParts.every((part, index) => {
            return part.startsWith(':') || part === currentParts[index];
        });
    }

    // useEffect(() => {
    //     const route = router.routes.find(route =>
    //         matchCustomPath(route.path, location.pathname)
    //     );
    //     console.log('route:', route);
    //     console.log('location.pathname:', location.pathname);
    //     if (route) {
    //         setCurrentHeader(route.header);
    //         setCurrentFooter(route.footer);
    //     }else{
    //         console.log("No matching route found for:", location.pathname);
    //     }
    //
    // }, [location]);

    useEffect(() => {
        console.log("App component rendered or re-rendered due to route or state changes");

        const route = router.routes.find(route =>
            matchCustomPath(route.path, location.pathname)
        );
        console.log('Matching route:', route);
        console.log('Current location:', location.pathname);

        if (route) {
            setCurrentHeader(route.header);
            setCurrentFooter(route.footer);
        } else {
            console.log("No matching route found for:", location.pathname);
        }
    }, [location]);  // Make sure `location` is the only dependency if it’s all that's needed



    const isOnboarding = location.pathname === '/onBoarding';

    return (
        <>
            <main className={isOnboarding ? 'noPadding' : ''}>
                {currentHeader}
                <Routes>
                    {router.routes.map((route, index) => (
                        <Route path={route.path} element={route.component} key={index} />
                    ))}
                </Routes>
                {currentFooter}
            </main>
        </>
    );
}

export default App;

