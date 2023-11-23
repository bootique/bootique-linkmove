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

package io.bootique.linkmove.v2;

import io.bootique.BQRuntime;
import io.bootique.cayenne.v42.CayenneModule;
import io.bootique.jdbc.JdbcModule;
import io.bootique.junit5.*;
import io.bootique.linkmove.v2.LinkMoveModule;
import io.bootique.linkmove.v2.LinkMoveModuleProvider;
import org.junit.jupiter.api.Test;

@BQTest
public class LinkMoveModuleProviderTest {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void autoLoadable() {
        BQModuleProviderChecker.testAutoLoadable(LinkMoveModuleProvider.class);
    }

    @Test
    public void metadata() {
        BQModuleProviderChecker.testMetadata(LinkMoveModuleProvider.class);
    }

    @Test
    public void moduleDeclaresDependencies() {
        final BQRuntime bqRuntime = testFactory.app().moduleProvider(new LinkMoveModuleProvider()).createRuntime();
        BQRuntimeChecker.testModulesLoaded(bqRuntime,
                JdbcModule.class,
                LinkMoveModule.class,
                CayenneModule.class
        );
    }
}
