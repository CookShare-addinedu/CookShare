import {NavLink} from "react-router-dom";
import './Menu.scss';
import {useDispatch, useSelector} from "react-redux";
import {clearFood} from "../../redux/foodSlice";

export default function Menu() {
    const food = useSelector((state) => state.food.value);
    const dispatch = useDispatch();

    const handleClearFood = () => {
        dispatch(clearFood());
    };
    return(
        <div className={'menu'}>
            <ul>
                <li className={'menu_list'}>
                    <NavLink
                        to={{
                            pathname: `/main/update/${food.foodId}`,
                            state: {food}
                        }}
                    >
                        수정
                    </NavLink>
                </li>
                <li className={'menu_list'}>
                    <NavLink
                        to={'/main'}
                        onClick={handleClearFood}
                    >
                        삭제
                    </NavLink>
                </li>
            </ul>
        </div>
    )
}