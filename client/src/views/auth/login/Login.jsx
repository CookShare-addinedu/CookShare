import './Login.scss'
import axios from "axios";
import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";

export default function Login (){
    const [FormData, setFormData] = useState({
        tel: '',
        password: ''
    });
    const [ShowPassword, setShowPassword] = useState(false);
    const handleChange = (event) => {
        setFormData(prevState => ({
            ...prevState,
            [event.target.name]: event.target.value
        }))
    }

    const handleToggle = (event) => {
        event.preventDefault();
        setShowPassword(!ShowPassword);
    }
    const handleSubmit = async (event) => {
        event.preventDefault();
        const {tel, password} = event.target.elements;
        const data = {
            tel: tel.value,
            password: password.value
        }
        try{
            const response = await axios.post('/api/user/login',data);

            if (response.status === 200) {
                const token =  response.data.token;
                localStorage.setItem('jwt', token);
                console.log('로그인 성공, JWT저장됨');
            }else{
                console.log('로그인 실패:', response.status, response.statusText);
            }

        }catch (error){
            console.log('서버 오류:', error);
        }
    }
    return(
        <form className={'login'} onSubmit={handleSubmit}>
            <div className={'field'}>
                <label htmlFor={'tel'} className={'a11y-hidden'}>휴대폰 번호</label>
                <input
                    type={'tel'}
                    id={'tel'}
                    name={'tel'}
                    placeholder="휴대폰 번호를 입력해주세요"
                    maxLength={11}
                    pattern={'[0-9]{2,3}-[0-9]{3,4}-[0-9]{4}'}
                    // pattern={'\d*'}
                    autoComplete={'off'}
                    onChange={handleChange}
                    required
                />
            </div>
            <div className={'field password_wrap'}>
                <label htmlFor={'password'} className={'a11y-hidden'}>비밀번호</label>
                <input
                    type={ShowPassword ? 'text' : 'password'}
                    id={'password'}
                    name={'password'}
                    placeholder="비밀번호를 입력해주세요"
                    autoComplete={'off'}
                    onChange={handleChange}
                    required
                />
                <button onClick={handleToggle}>
                    <span>
                        {ShowPassword ? <FontAwesomeIcon icon={faEye}/> : <FontAwesomeIcon icon={faEyeSlash}/>}
                    </span>
                </button>
            </div>
            <div className={'field login_wrap'}>
                <button type="submit">
                    <span>로그인</span>
                </button>
            </div>
        </form>
    )
}