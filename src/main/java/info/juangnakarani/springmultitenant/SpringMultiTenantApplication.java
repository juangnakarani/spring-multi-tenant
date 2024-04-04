package info.juangnakarani.springmultitenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class SpringMultiTenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMultiTenantApplication.class, args);
	}

}
