/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 12/07/25
 */
package com.dailyminutes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

/**
 * The type Dailyminutes application.
 */
@Modulithic(sharedModules = {"laundry.customer"})
@SpringBootApplication
public class DailyminutesApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
		SpringApplication.run(DailyminutesApplication.class, args);
	}

}
