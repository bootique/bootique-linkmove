/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.linkmove.v4.json;

import com.nhl.link.move.LmTask;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.cayenne.v50.CayenneModule;
import io.bootique.cayenne.v50.junit.CayenneTester;
import io.bootique.jdbc.junit.derby.DerbyTester;
import io.bootique.junit.BQApp;
import io.bootique.junit.BQTest;
import io.bootique.junit.BQTestTool;
import io.bootique.linkmove.v4.LinkMoveModule;
import io.bootique.linkmove.v4.json.cayenne.Table1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class LinkMoveJSONIT {

    @BQTestTool
    static final DerbyTester db = DerbyTester.db();

    @BQTestTool
    static final CayenneTester cayenne = CayenneTester.create().entities(Table1.class);

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique
            .app("-c", "classpath:io/bootique/linkmove/v4/json/test.yml")
            .autoLoadModules()
            .module(db.moduleWithTestDataSource("ds"))
            .module(cayenne.moduleWithTestHooks())
            .module(b -> LinkMoveModule.extend(b).addResourceConnector("data", "classpath:io/bootique/linkmove/v4/json/data.json"))
            .module(b -> CayenneModule.extend(b).addLocation("classpath:io/bootique/linkmove/v4/json/cayenne-project.xml"))
            .createRuntime();


    @Test
    public void linkMoveJSON() {
        LmTask task = app.getInstance(LmRuntime.class)
                .getTaskService()
                .createOrUpdate(Table1.class)
                .sourceExtractor("extractor.xml")
                .task();

        assertEquals(5, task.run().getStats().getCreated());
        assertEquals(0, task.run().getStats().getCreated());
    }

}
