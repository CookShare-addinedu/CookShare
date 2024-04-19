import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse, faComments, faStore} from "@fortawesome/free-solid-svg-icons";
export default function Footer() {
    return (
        <footer>
            <ul>
                <li>
                    <a href={'#'}>
                        <span>
                            <FontAwesomeIcon icon={faHouse} />
                        </span>
                    </a>
                </li>
                <li>
                    <a href={'#'}>
                        <span>
                            <FontAwesomeIcon icon={faComments} />
                        </span>
                    </a>
                </li>
                <li>
                    <a href={'#'}>
                        <span>
                            <FontAwesomeIcon icon={faStore}/>
                        </span>
                    </a>
                </li>
            </ul>
        </footer>
    );
}