import Address from "../../../components/adress/Address";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {setAddress} from "../../../redux/addressSlice";
import {useNavigate} from "react-router-dom";

export default function SearchAddress() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const [addressLocal, setAddressLocal] = useState('');
    const handleLocationSelect = (selectAddress) => {
        setAddressLocal(selectAddress);
        dispatch(setAddress(selectAddress));
        navigate('/main');
    }
    return (
        <div>
            <Address onLocationSelect={handleLocationSelect}/>
        </div>
    )
}