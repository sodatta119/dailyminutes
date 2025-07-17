package com.dailyminutes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.modulith.core.ApplicationModules;

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
        ApplicationModules.of(DailyminutesApplication.class).verify();
	}

}
