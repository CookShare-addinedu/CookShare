import './Login.scss'
import axios from "axios";
import * as PropTypes from "prop-types";

export default function Login (){
    const handleSubmit = async (event) => {
        event.preventDefault();
        const {tel, password} = event.target.elements;
        // const formData = new FormData(event.target);
        // const data = {
        //     tel: formData.get('tel'),
        //     password: formData.get('password')
        // }
        const data = {
            tel: tel.value,
            password: password.value
        }
        try{
            const response = await axios.post('/api/login',data);

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
                <input type="tel" id={'tel'} name={'tel'} placeholder="id" required/>
            </div>
            <div className={'field'}>
                <label htmlFor={'password'} className={'a11y-hidden'}>비밀번호</label>
                <input type="password" name={'password'} placeholder="password" required/>
            </div>
            <div className={'field login_btn'}>
                <button type="submit">
                    <span>로그인</span>
                </button>
            </div>
        </form>
    )
}