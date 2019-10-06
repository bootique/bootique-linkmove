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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.nhl.link.move.LmTask;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQCoreModule;
import io.bootique.cayenne.v41.CayenneModule;
import io.bootique.cli.Cli;
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import io.bootique.jersey.JerseyModule;
import io.bootique.linkmove.rest.cayenne41.cayenne.Table1;
import io.bootique.test.junit.BQTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import static org.junit.Assert.assertEquals;

public class LinkMoveRestIT {

    @ClassRule
    public static BQTestFactory sharedTestFactory = new BQTestFactory();

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @BeforeClass
    public static void startJersey() {
        sharedTestFactory
                .app("-s")
                .autoLoadModules()
                .module(b -> JerseyModule.extend(b).addResource(R1.class))
                .run();
    }

    @Test
    public void testLinkMoveRest() {
        testFactory
                .autoLoadModules()
                .app("-c", "classpath:io/bootique/linkmove/rest/cayenne41/test.yml")
                .module(b -> BQCoreModule.extend(b).setDefaultCommand(LMWithRest.class))
                .module(b -> CayenneModule.extend(b).addProject("io/bootique/linkmove/rest/cayenne41/cayenne-project.xml"))
                .run();
    }

    // TODO: probably worth checking the exception message .. ProvisionException can be thrown for unrelated reasons
    @Test(expected = ProvisionException.class)
    public void testLinkMoveRest_ConflictingConnectorTypes() {
        testFactory
                .autoLoadModules()
                .app("-c", "classpath:io/bootique/linkmove/rest/cayenne41/test-conflicting-connectors.yml")
                .module(b -> BQCoreModule.extend(b).setDefaultCommand(LMWithRest.class))
                .module(b -> CayenneModule.extend(b).addProject("io/bootique/linkmove/rest/cayenne41/cayenne-project.xml"))
                .run();
    }

    public static final class LMWithRest implements Command {

        @Inject
        private Provider<LmRuntime> runtime;

        @Override
        public CommandOutcome run(Cli cli) {

            LmTask task = runtime.get()
                    .getTaskService()
                    .createOrUpdate(Table1.class)
                    .sourceExtractor("extractor.xml")
                    .task();

            assertEquals(5, task.run().getStats().getCreated());
            assertEquals(0, task.run().getStats().getCreated());

            return CommandOutcome.succeeded();
        }
    }

    @Path("/r1")
    public static class R1 {

        @Context
        private Configuration config;

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public String get(@Context UriInfo uriInfo) {
            return "{\n" +
                    "  \"data\": [\n" +
                    "    {\n" +
                    "      \"id\": 1,\n" +
                    "      \"name\": \"n1\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 2,\n" +
                    "      \"name\": \"n2\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 3,\n" +
                    "      \"name\": \"n3\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 4,\n" +
                    "      \"name\": \"n4\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 5,\n" +
                    "      \"name\": \"n5\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        }
    }
}
