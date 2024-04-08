export default function Register(){
    return(
        <form>
            <div>
                <input type="text" id={"nickname"} name={"nickname"} placeholder="닉네임을 입력하세요" />
                <button type="submit">중복체크</button>
            </div>
            <input type="password" id={"password"} name={"password"} placeholder="비밀번호를 입력하세요" />
            <input type="password" id ={"passwordCheck"} name={"passwordCheck"} placeholder="비밀번호를 입력하세요" />
            <input type="text" id="address" name="address" placeholder="주소를 입력해주세요" />
            <div>
                <input type="text" id="phoneNumber" name="phoneNumber" placeholder="휴대폰 번호를 입력해주세요" />
                <button type="submit">인증번호 재전송</button>
            </div>
            <div>
                <input type="text" id="authNumber" name="authNumber" placeholder="인증번호 입력" />
                <span>03:00</span>
            </div>
            <button type="submit">회원가입</button>
        </form>
    )
}