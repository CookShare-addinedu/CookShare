import React from "react";
import './App.css'
import { Routes ,Route } from "react-router-dom";
import Home from "./views/auth/Home";
import Login from "./views/auth/Login";
import Welcome from "./views/auth/Welcome";
import Register from "./views/auth/Register";

function App() {
  return (
    <div className="App">
  {/*<h1>푸드쉐어페이지입니다.</h1>*/}
      <Routes>
          <Route path={"/"} element={<Home />} />
          <Route path={"/Login"} element={<Login />} />
          <Route path={"/Welcome"} element={<Welcome />} />
          <Route path={"/Register"} element={<Register />} />
          {/*<Route path={"/Profile"} element={<Profile />} />*/}
      </Routes>
    </div>
  )
}

export default App;
