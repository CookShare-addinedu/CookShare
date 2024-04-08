import Cards from "../../../components/card/Cards";
import {useState} from "react";
import {FoodList} from "../../../data/FoodList";

export default function Board(){
    const cards = [1, 2, 3, 4, 5];
    // let [card, setCard] = useState(FoodList);

    return(
        <div>
            {cards.map((card, index) => (
                <Cards key={index}/>
            ))}
        </div>
    )
}