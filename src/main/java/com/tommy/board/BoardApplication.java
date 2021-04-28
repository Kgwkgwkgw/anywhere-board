package com.tommy.board;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class BoardApplication {
	public static void main(String[] args) {
		// https://www.programmersought.com/article/8531993223/
		// 빈으로 등록 시, 커넥션에러가 발생하여 아래와 같이 개발하였음.
		if ("local".equals(System.getProperty("spring.profiles.active"))) {
			try {
				Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092", "-ifNotExists").start();
			} catch (SQLException throwables) {
			    // @Todo 로깅으로 변경하기
			    System.out.println("h2 db start up error");
			}
		}
		SpringApplication.run(BoardApplication.class, args);
	}

}
