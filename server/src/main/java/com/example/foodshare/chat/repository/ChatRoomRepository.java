package com.example.foodshare.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.foodshare.domain.ChatRoom;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
	//List<ChatRoom> findByFirstUserOrSecondUser(String firstUserId, String secondUserId);

	// @Query("{ $or: [ { 'firstUser': ?0 }, { 'secondUser': ?0 } ] }")
	// List<ChatRoom> findByFirstUserOrSecondUser(String firstUserId, String secondUserId);

	@Query("{'_id': {$regex: ?0, $options: 'i'}}")
	List<ChatRoom> findByIdContaining(String userId);

	// @Query("{'id': ?0, '$or': [{'firstUser': ?1}, {'secondUser': ?1}]}")
	// Page<ChatRoom.ChatMessages> findMessagesByRoomId(String roomId, String userId, Pageable pageable);
	// // @Query("{'id': ?0}")
	// // Page<ChatRoom> findMessagesByRoomId(String roomId, Pageable pageable);

	// @Query("{'id': ?0}")
	// Page<ChatRoom> findMessagesByRoomId(String roomId, Pageable pageable);

	// 새로운 메소드: 채팅방 ID에 따라 메시지 페이지네이션
	@Query(value = "{ 'id' : ?0 }", fields = "{ 'content' : 1 }")
	Page<ChatRoom.ChatMessages> findMessagesByRoomId(String roomId, Pageable pageable);

}

