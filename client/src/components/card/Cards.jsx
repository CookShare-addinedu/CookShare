import {Box, Card, Flex, Text} from '@radix-ui/themes';
import './Cards.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHeart} from "@fortawesome/free-regular-svg-icons";
import Badge from "../badge/Badge";
export default function Cards({food}) {
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
                                    <p className={'date'}>{food.createdAt}1시간전</p>
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
                                </p>
                            </div>
                        </Box>
                    </Flex>
                </Card>
            </Box>
        </Flex>
    )
}