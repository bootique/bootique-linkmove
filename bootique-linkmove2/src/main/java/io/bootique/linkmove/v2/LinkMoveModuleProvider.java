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

import io.bootique.BQModuleProvider;
import io.bootique.bootstrap.BuiltModule;
import io.bootique.cayenne.v42.CayenneModuleProvider;
import io.bootique.jdbc.JdbcModuleProvider;

import java.util.Collection;

import static java.util.Arrays.asList;

/**
 * @since 2.0
 * @deprecated in favor of LinkMove v3
 */
@Deprecated(since = "3.0", forRemoval = true)
public class LinkMoveModuleProvider implements BQModuleProvider {

    @Override
    public BuiltModule buildModule() {
        return BuiltModule.of(new LinkMoveModule())
                .provider(this)
                .description("Deprecated and can be replaced with 'bootique-linkmove3'.")
                .config("linkmove", LinkMoveFactory.class)
                .build();
    }

    @Override
    @Deprecated(since = "3.0", forRemoval = true)
    public Collection<BQModuleProvider> dependencies() {
        return asList(
                new JdbcModuleProvider(),
                new CayenneModuleProvider()
        );
    }
}
