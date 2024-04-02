package com.example.foodshare.chat.repository;// package com.example.foodshare.chat.repository;
//
// import static org.mockito.Mockito.*;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.Optional;
//
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
// import com.example.foodshare.domain.ChatRoom;
//
// @DataJpaTest
// public class ChatRepositoryTest {
//
// 	@Autowired
// 	private ChatRepository chatRepository;
//
// 	@Test
// 	public void whenFindById_thenReturnChatRoom() {
// 		// given
// 		ChatRoom chatRoom = new ChatRoom();
// 		chatRoom.setId("testRoomId");
// 		chatRepository.save(chatRoom);
//
// 		// when
// 		Optional<ChatRoom> found = chatRepository.findById(chatRoom.getId());
//
// 		// then
// 		assertThat(found.isPresent()).isTrue();
// 		assertThat(found.get().getId()).isEqualTo(chatRoom.getId());
// 	}
//
// 	@Test
// 	public void whenInvalidId_thenReturnEmpty() {
// 		// given
// 		String invalidId = "nonExistingId";
//
// 		// when
// 		Optional<ChatRoom> result = chatRepository.findById(invalidId);
//
// 		// then
// 		assertThat(result.isPresent()).isFalse();
// 	}
// }