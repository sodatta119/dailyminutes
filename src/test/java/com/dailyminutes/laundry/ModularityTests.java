/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 23/07/25
 */
package com.dailyminutes.laundry;

import com.dailyminutes.DailyminutesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;


/**
 * The type Modularity tests.
 */
public class ModularityTests {
    /**
     * The Modules.
     */
    ApplicationModules modules = ApplicationModules.of(DailyminutesApplication.class);

    /**
     * Verify modularity.
     */
    @Test
    void verifyModularity() {
        //System.out.println(modules.toString());
        modules.verify();
    }

}
