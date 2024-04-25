import './Login.scss'
import axios from "axios";
import {useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleRight, faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";
import {NavLink} from "react-router-dom";

export default function Login (){
    const [FormData, setFormData] = useState({
        tel: '',
        password: ''
    });
    const [ShowPassword, setShowPassword] = useState(false);
    const [Error, setError] = useState('');

    function formatPhoneNumber(value) {
        const numbers = value.replace(/[^\d]/g, '');

        if (numbers.length < 4) return numbers;
        if (numbers.length < 7) return `${numbers.slice(0, 3)}-${numbers.slice(3)}`;
        if (numbers.length < 11) return `${numbers.slice(0, 3)}-${numbers.slice(3, 6)}-${numbers.slice(6)}`;

        return `${numbers.slice(0, 3)}-${numbers.slice(3, 7)}-${numbers.slice(7)}`;
    }

    const handleChange = (event) => {
        const { name, value } = event.target;
        const formattedValue = name === 'tel' ? formatPhoneNumber(value) : value;

        setFormData(prevState => ({
            ...prevState,
            [name]: formattedValue
        }));
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

            if (response.status === 200 && response.data.token) {
                const token =  response.data.token;
                localStorage.setItem('jwt', token);
                console.log(token);
                console.log('로그인 성공, JWT 저장됨:', token);

                console.log('LocalStorage에 저장된 토큰:', localStorage.getItem('jwt'));
            }else{
                console.log('로그인 실패:', response.status, response.statusText);
            }

        }catch (error){
            if(error.response){
                setError(' 휴대폰번호 또는 비밀번호를 잘못 입력했습니다.\n' +
                    '입력하신 내용을 다시 확인해주세요.');
            }
            console.log('서버 오류:', error);
        }
    }

    return(
        <section className={'login'}>
            <div className={'logo_wrap'}>
                <img src={'../../img/cookshare.svg'} alt={'logo'} />
            </div>
            <form className={'login_form'} onSubmit={handleSubmit}>
                <div className={'field'}>
                    <label htmlFor={'tel'} className={'a11y-hidden'}>휴대폰 번호</label>
                    <input
                        type={'tel'}
                        id={'tel'}
                        name={'tel'}
                        value={FormData.tel}
                        placeholder="휴대폰 번호를 입력해주세요"
                        maxLength={13}
                        pattern={'[0-9]{3}-[0-9]{3,4}-[0-9]{4}'}
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
                        value={FormData.password}
                        placeholder="비밀번호를 입력해주세요"
                        maxLength={12}
                        autoComplete={'off'}
                        onChange={handleChange}
                        required
                    />
                    <button onClick={handleToggle}>
                        <span>
                            <FontAwesomeIcon icon={ShowPassword ?  faEye : faEyeSlash}/>
                        </span>
                    </button>
                </div>

                {Error && <div className="error">{Error}</div>}

                <div className={'field login_wrap'}>
                    <button type="submit">
                        <span>로그인</span>
                    </button>
                </div>
                <div className={'field register_wrap'}>
                    <p>아직도 회원이 아니세요?</p>
                    <button type="button">
                        <NavLink to={'/register'}>
                            <span>회원가입</span>
                            <FontAwesomeIcon icon={faAngleRight}/>
                        </NavLink>
                    </button>
                </div>
            </form>
        </section>
    )
}