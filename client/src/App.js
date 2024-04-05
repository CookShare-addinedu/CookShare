import React from "react";
import './App.css'
import { Routes ,Route } from "react-router-dom";
import Home from "./views/auth/Home";
import Login from "./views/auth/Login";
import SigninSignup from "./views/auth/SigninSignup";
import SignUp from "./views/auth/SignUp";

function App() {
  return (
    <div className="App">
  {/*<h1>푸드쉐어페이지입니다.</h1>*/}
      <Routes>
          <Route path={"/"} element={<Home />} />
          <Route path={"/Login"} element={<Login />} />
          <Route path={"/SigninSignup"} element={<SigninSignup />} />
          <Route path={"/Signup"} element={<SignUp />} />
      </Routes>
    </div>
  )
}

export default App;
