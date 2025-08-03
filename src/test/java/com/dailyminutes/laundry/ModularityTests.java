/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 23/07/25
 */
package com.dailyminutes.laundry;

import com.dailyminutes.DailyminutesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;


public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(DailyminutesApplication.class);

    @Test
    void verifyModularity() {
        //System.out.println(modules.toString());
        modules.verify();
    }

}
