import React from "react";
import './Button.css'

const Button = ({text, onClick}) => {
    return <button className={'button'} onClick={onClick}>{text}</button>
};

export const loginButton = ({text, onClick}) => {
    return <button className={'loginButton'} onClick={onClick}>{text}</button>
};

export const signupButton = ({text, onClick}) => {
    return <button className={'signupButton'} onClick={onClick}>{text}</button>
};

export  default Button;