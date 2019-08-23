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

package io.bootique.linkmove.json;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.nhl.link.move.LmTask;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQCoreModule;
import io.bootique.cayenne.CayenneModule;
import io.bootique.cli.Cli;
import io.bootique.command.Command;
import io.bootique.command.CommandOutcome;
import io.bootique.linkmove.json.cayenne.Table1;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LinkMoveJSONIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testLinkMoveJSON() {
        testFactory
                .autoLoadModules()
                .app("-c", "classpath:io/bootique/linkmove/json/test.yml")
                .module(b -> BQCoreModule.extend(b).setDefaultCommand(LMWithJson.class))
                .module(b -> CayenneModule.extend(b).addProject("io/bootique/linkmove/json/cayenne-project.xml"))
                .run();
    }

    public static final class LMWithJson implements Command {

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
}
