package com.dailyminutes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * The type Dailyminutes application tests.
 */
@SpringBootTest
class DailyminutesApplicationTests {

    @Autowired
    ApplicationContext context;

    /**
     * Context loads.
     */
    @Test
    void contextLoads() {
//        try {
//            ApplicationModules modules=ApplicationModules.of(DailyminutesApplication.class).verify();
//        }catch (Throwable ex) {
//            ex.printStackTrace(); // Should show the dependency trace
//            throw ex;
//        }
    }

}
