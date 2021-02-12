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

package io.bootique.linkmove.rest.cayenne41;

import com.nhl.link.move.LmTask;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.cayenne.v41.CayenneModule;
import io.bootique.jdbc.junit5.derby.DerbyTester;
import io.bootique.jersey.JerseyModule;
import io.bootique.junit5.BQApp;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import io.bootique.linkmove.rest.cayenne41.cayenne.Table1;
import org.junit.jupiter.api.Test;

import javax.ws.rs.*;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class LinkMoveRestIT {

    @BQApp
    static final BQRuntime server = Bootique
            .app("-s")
            .autoLoadModules()
            .module(b -> JerseyModule.extend(b).addResource(R1.class))
            .createRuntime();

    @BQTestTool
    static final DerbyTester db = DerbyTester.db()
            .initDB("classpath:io/bootique/linkmove/rest/cayenne41/schema.sql")
            .deleteBeforeEachTest("table1");

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testLinkMoveRest() {

        BQRuntime lm1 = testFactory
                .app("-c", "classpath:io/bootique/linkmove/rest/cayenne41/test.yml")
                .autoLoadModules()
                .module(db.moduleWithTestDataSource("ds"))
                .module(b -> CayenneModule.extend(b).addProject("io/bootique/linkmove/rest/cayenne41/cayenne-project.xml"))
                .createRuntime();

        LmTask task = lm1.getInstance(LmRuntime.class)
                .getTaskService()
                .createOrUpdate(Table1.class)
                .sourceExtractor("extractor.xml")
                .task();

        assertEquals(5, task.run().getStats().getCreated());
        assertEquals(0, task.run().getStats().getCreated());
    }

    @Test
    public void testLinkMoveRest_ResolvePathParameters() {

        BQRuntime lm2 = Bootique
                .app("-c", "classpath:io/bootique/linkmove/rest/cayenne41/test-params.yml")
                .autoLoadModules()
                .module(db.moduleWithTestDataSource("ds"))
                .module(b -> CayenneModule.extend(b).addProject("io/bootique/linkmove/rest/cayenne41/cayenne-project.xml"))
                .createRuntime();

        LmTask task = lm2.getInstance(LmRuntime.class)
                .getTaskService()
                .createOrUpdate(Table1.class)
                .sourceExtractor("extractor.xml")
                .task();

        Map<String, Object> params = RestConnector.bindTemplateValues()
                .value("p1", 897L)
                .value("p2", "a_Name")
                .toExtractorParameters();

        assertEquals(1, task.run(params).getStats().getCreated());

        // TODO: check the data in DB - it must have used parameters above
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public String get() {
            return "{\"data\": [" +
                    "    {" +
                    "      \"id\": 1," +
                    "      \"name\": \"n1\"" +
                    "    }," +
                    "    {" +
                    "      \"id\": 2," +
                    "      \"name\": \"n2\"" +
                    "    }," +
                    "    {" +
                    "      \"id\": 3," +
                    "      \"name\": \"n3\"" +
                    "    }," +
                    "    {" +
                    "      \"id\": 4," +
                    "      \"name\": \"n4\"" +
                    "    }," +
                    "    {" +
                    "      \"id\": 5," +
                    "      \"name\": \"n5\"" +
                    "    }" +
                    "]}";
        }

        @GET
        @Path("{id}")
        @Produces(MediaType.APPLICATION_JSON)
        public String getOne(@PathParam("id") long id, @QueryParam("name") String name) {
            return "{\"data\": [" +
                    "    {" +
                    "      \"id\": " + id + "," +
                    "      \"name\": \"" + name + "\"" +
                    "    }" +
                    "]}";
        }
    }
}
