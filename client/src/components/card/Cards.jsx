import {Box, Card, Flex, Text} from '@radix-ui/themes';
export default function Cards() {
    return(
        <Flex gap="3" direction="column">
            <Box width="350px">
                <Card size="1">
                    <Flex gap="3" align="center">
                        <img
                            src="https://images.unsplash.com/photo-1617050318658-a9a3175e34cb?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=600&q=80"
                            alt="Bold typography"
                            style={{
                                display: 'block',
                                objectFit: 'cover',
                                width: '20%',
                                height: 140,
                                backgroundColor: 'var(--gray-5)',
                            }}
                        />
                        <Box>
                            <Text as="div" size="2" weight="bold">
                                Teodros Girmay
                            </Text>
                            <Text as="div" size="2" color="gray">
                                Engineering
                            </Text>
                        </Box>
                    </Flex>
                </Card>
            </Box>
        </Flex>
    )
}