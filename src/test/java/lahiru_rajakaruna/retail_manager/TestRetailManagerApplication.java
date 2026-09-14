package lahiru_rajakaruna.retail_manager;

import org.springframework.boot.SpringApplication;

public class TestRetailManagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(RetailManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
