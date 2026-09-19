package lahiru_rajakaruna.retail_manager;

import lahiru_rajakaruna.retail_manager.Config.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestRetailManagerApplication {

    public static void main(String[] args) {
        SpringApplication.from(RetailManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
