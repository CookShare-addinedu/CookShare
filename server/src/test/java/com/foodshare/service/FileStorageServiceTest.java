package com.foodshare.service;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.foodshare.board.exception.FileStorageException;
import com.foodshare.board.service.FileStorageService;

@SpringBootTest
public class FileStorageServiceTest {

	@Autowired
	private FileStorageService fileStorageService;

	@MockBean
	private Path mockPath;

	@Test
	void storeFile_success() {
		// 테스트 파일 생성
		MultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());

		// 파일 저장을 시도하고 결과 파일 이름을 받아옴
		String fileName = null;
		try {
			fileName = fileStorageService.storeFile(mockFile);
		} catch (FileStorageException e) {
			throw new RuntimeException(e);
		}

		// 파일 이름이 타임스탬프와 기대하는 형식("_filename.txt")을 따르는지 검증
		String regex = "\\d+_filename\\.txt";
		assertTrue(fileName.matches(regex), "The file name should match the pattern: timestamp_filename.txt");
	}
}
