import './Login.scss'
export default function Login (){
    return(
        <form className={'login'}>
            <div className={'field'}>
                <label htmlFor={'tel'} className={'a11y-hidden'}>휴대폰 번호</label>
                <input type="tel" id={'tel'} name={'tel'} placeholder="아이디를 입력하세요" required/>
            </div>
            <div className={'field'}>
                <label htmlFor={'password'} className={'a11y-hidden'}>비밀번호</label>
                <input type="password" placeholder="비밀번호를 입력하세요" required/>
            </div>
            <div className={'field login_btn'}>
                <button type="submit">
                    <span>로그인</span>
                </button>
            </div>
        </form>
    )
}