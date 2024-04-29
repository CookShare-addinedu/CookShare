import './Search.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft} from "@fortawesome/free-solid-svg-icons";
import {useNavigate} from "react-router-dom";
import {faCircleXmark} from "@fortawesome/free-solid-svg-icons/faCircleXmark";
import {useState} from "react";

export default function Search() {
    const navigate = useNavigate();
    const [inputValue, setInputValue] = useState('');
    const handleClear = () => {
        setInputValue('');
    }
    const handleInput = (event) => {
        setInputValue(event.target.value);
    }
    return (
        <div className={'search_address'}>
            <div className={'prev'}>
                <button onClick={() => navigate(-1)}>
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft}/>
                    </span>
                </button>
            </div>
            <div className={'search'}>
                <input
                    type={'text'}
                    value={inputValue}
                    placeholder={'검색어를 입력해주세요'}
                    onChange={handleInput}
                />
                {inputValue &&
                <button onClick={handleClear}>
                    <FontAwesomeIcon icon={faCircleXmark} />
                </button>
                }
            </div>
        </div>
    )
}