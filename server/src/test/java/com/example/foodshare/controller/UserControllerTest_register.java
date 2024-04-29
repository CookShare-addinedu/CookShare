// package com.example.foodshare.controller;
//
// import com.example.foodshare.domain.User;
// import com.example.foodshare.service.UserService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.foodshare.domain.User;
// import com.foodshare.security.service.UserService;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
//
// import java.sql.Timestamp;
// import java.util.Optional;
//
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.mockito.BDDMockito.given;
//
// @SpringBootTest
// @AutoConfigureMockMvc(addFilters = false)
// public class UserControllerTest_register {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@MockBean
// 	private UserService userService;
//
// 	@Autowired
// 	private ObjectMapper objectMapper;
//
// 	private User user;
//
// 	@BeforeEach
// 	void setUp() {
// 		// 사용자 정보 초기화
// 		user = new User();
// 		user.setMobileNumber("01012345678");
// 		user.setNickName("TestUser");
// 		user.setPassword("password");
// 		user.setLocation("test");
// 		user.setCreatedAt(Timestamp.valueOf("2024-04-07 18:58:11.229835"));
// 		user.setUpdatedAt(Timestamp.valueOf("2024-04-07 18:58:11.229835"));
//
// 	}
//
// 	@Test
// 	void whenRegisterUserWithExistingMobileNumber_thenBadRequest() throws Exception {
// 		// 사용자 서비스를 모의하여 이미 존재하는 번호로 설정
// 		given(userService.findByMobileNumber(user.getMobileNumber())).willReturn(Optional.of(user));
//
// 		mockMvc.perform(post("/api/user/register")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(user)))
// 			.andExpect(status().isBadRequest());
// 	}
//
// 	@Test
// 	void whenRegisterUserWithNewMobileNumber_thenOk() throws Exception {
// 		// 사용자 서비스를 모의하여 새 번호로 설정
// 		given(userService.findByMobileNumber(user.getMobileNumber())).willReturn(Optional.empty());
// 		given(userService.registerUser(Mockito.any(User.class))).willReturn(user);
//
// 		mockMvc.perform(post("/api/user/register")
// 				.contentType(MediaType.APPLICATION_JSON)
// 				.content(objectMapper.writeValueAsString(user)))
// 			.andExpect(status().isOk());
// 	}
// }
