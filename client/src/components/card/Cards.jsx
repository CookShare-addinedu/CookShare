import {Box, Card, Flex, Text} from '@radix-ui/themes';
import './Cards.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import { faHeart as faSolidHeart } from "@fortawesome/free-solid-svg-icons"; // 실제 하트 아이콘 추가

import Badge from "../badge/Badge";
import { formatDistanceToNow, parseISO } from 'date-fns';
import { ko } from 'date-fns/locale';
import axios from "axios";
import {useState} from "react";
export default function Cards({food}) {

    // isFavorite을 기반으로 isFavorited 상태 초기화
    const [isFavorited, setIsFavorited] = useState(food.isFavorite);
    function formatTimeAgo(dateStr) {

        const date = parseISO(dateStr);  // 서버에서 'yyyy-MM-dd' 형식의 문자열로 받은 날짜를 Date 객체로 변환
        return formatDistanceToNow(date, { addSuffix: true, locale: ko });  // 현재 시간과의 차이를 자연스럽게 표현
    }

    return(
        <Flex className={'card_wrap'} gap="3" direction="column">
            <Box className={'card_box'}>
                <Card className={'card'} size="1">
                    <Flex className={'card_layout'} gap="3" align="center">
                        <div className={'img_wrap'}>
                            <img
                                src={food.imageUrls?.[0] || './../img/cookshare.svg'}
                                alt={food.title}
                            />
                        </div>
                        <Box className={'content'}>
                            <div className={'text_wrap'}>
                                <div className={'title'}>
                                    <h4>{food.title}</h4>
                                    <p className={'date'}>{formatTimeAgo(food.createdAt)}</p>
                                </div>
                                <div className={'description'}>
                                    <p>{food.description}</p>
                                    <p>
                                        <span>#피자 #나눔</span>
                                    </p>
                                </div>
                            </div>
                            <div className={'icon_wrap'}>
                                <Badge food={food}/>
                                <p className={'like'}>

                                    {/* isFavorited에 따라 아이콘 색상 변경 */}
                                    <FontAwesomeIcon icon={isFavorited ? faSolidHeart : faHeart} color={isFavorited ? 'red' : 'grey'}/>
                                    {/*<FontAwesomeIcon icon={faHeart}/>*/}
                                    <span>{food.likes}</span>
                                </p>
                            </div>
                        </Box>
                    </Flex>
                </Card>
            </Box>
        </Flex>
    )
}