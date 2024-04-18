import './Header.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft, faEllipsisVertical, faMagnifyingGlass, faBell, faUser} from "@fortawesome/free-solid-svg-icons";
import useHeaderTitle from "../../../hook/useHeaderTitle";



function Header1() {
    return (
        <header className={'header1'}>
            <div className={'prev'}>
                <a href="#">
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft} />
                    </span>
                </a>
            </div>
        </header>
    );
}
function Header2() {
    const title = useHeaderTitle();

    return (
        <header className={'header2'}>
            <div className={'prev'}>
                <a href="#">
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft} />
                    </span>
                </a>
            </div>
            <div className={'title'}>
                <h1>{title}</h1>
            </div>
        </header>
    );
}

function Header3() {
    const title = useHeaderTitle();
    return (
        <header className={'header3'}>
            <div className={'title'}>
                <h1>{title}</h1>
            </div>
        </header>
    )
}
function Header4() {
    const title = useHeaderTitle();
    return(
        <header className={'header4'}>
            <div className={'prev'}>
                <a href="#">
                    <span>
                        <FontAwesomeIcon icon={faAngleLeft} />
                    </span>
                </a>
            </div>
            <div className={'title'}>
                <h1>{title}</h1>
            </div>
            <div className={'menu'}>
                <button>
                    <span>
                        <FontAwesomeIcon icon={faEllipsisVertical}/>
                    </span>
                </button>
            </div>
        </header>
    )
}
function Header5(){
    return(
        <header className={'header5'}>
            <div className={'select_wrap'}>
                <select>
                    <option selected>가산동</option>
                    <option>2</option>
                    <option>3</option>
                </select>
            </div>
            <div className={'menu'}>
                <ul>
                    <li>
                        <a href={"#"}>
                            <FontAwesomeIcon icon={faMagnifyingGlass}/>
                        </a>
                    </li>
                    <li>
                        <a href={"#"}>
                            <FontAwesomeIcon icon={faUser} />
                        </a>
                    </li>
                    <li>
                        <a href={"#"}>
                            <FontAwesomeIcon icon={faBell}/>
                        </a>
                    </li>
                </ul>
            </div>
        </header>
    )
}


export {Header1, Header2, Header3, Header4, Header5};