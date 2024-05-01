import {Box, Card, Flex, Text} from '@radix-ui/themes';
import './Cards.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import Badge from "../badge/Badge";
import { formatDistanceToNow, parseISO } from 'date-fns';
import { ko } from 'date-fns/locale';
import axios from "axios";
export default function Cards({food}) {
    function formatTimeAgo(dateStr) {

        const date = parseISO(dateStr);  // 서버에서 'yyyy-MM-dd' 형식의 문자열로 받은 날짜를 Date 객체로 변환
        return formatDistanceToNow(date, { addSuffix: true, locale: ko });  // 현재 시간과의 차이를 자연스럽게 표현
    }

    const handleLike = async (foodId) => {
        const userId = 1; // 예시용 사용자 ID
        const isFavorite = true; // 찜하기 상태, 실제로는 상태에 따라 변경될 수 있음

        try {
            const response = await axios.post('/api/favorite', { foodId, userId, isFavorite });
            console.log('Like status updated:', response.data);
            // 여기에서 찜하기 상태 업데이트 로직 추가
        } catch (error) {
            console.error('Failed to update like status:', error);
        }
    };

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

                                    <FontAwesomeIcon icon={faHeart}/>
                                    <span>1</span>
                                    <span>{food.likes}</span>
                                    {/*<button onClick={() => handleLike(food.foodId)}>*/}
                                    {/*    <FontAwesomeIcon icon={food.isFavorite ? solidHeart : regularHeart}/>*/}
                                    {/*    <span>{food.likesCount}</span>*/}
                                    {/*</button>*/}
                                </p>
                            </div>
                        </Box>
                    </Flex>
                </Card>
            </Box>
        </Flex>
    )
}