package org.example.p2pcdn_backserver2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class P2pcdnBackserver2Application {

	public static void main(String[] args) {
		SpringApplication.run(P2pcdnBackserver2Application.class, args);
	}

}
