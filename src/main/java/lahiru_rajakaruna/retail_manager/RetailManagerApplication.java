package lahiru_rajakaruna.retail_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RetailManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailManagerApplication.class, args);
    }

}

// TODO: GLOBAL ERROR HANDLING
// TODO: AUTH PAGE ROUTING AND FORM SWITCHING
// TODO: PREPARE A DB ON A CONTAINER
// TODO: PREPARE REDIS CLUSTER CONTAINER
// TODO: BUILD CI PIPELINE
// TODO: BUILD CD PIPELINE
// TODO: INTEGRATE FLYWAY
// TODO: ADD 404 PAGE
// TODO: ADD ERROR PAGE
// TODO: SWAP RANDOM UUID WITH TIME SERIES UUID TO IMPROVE DATABASE PERFORMANCE ON INDEX TREE
// TODO: UPDATE THE CONTROLLERS TO NOT TO TAKE SHOPID FROM REQUESTS; USE THE SESSION;
// TODO: CHECK THE OLD MODULES AND UPDATE
