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
package io.bootique.linkmove.v3.rest;

import io.bootique.BQModuleProvider;
import io.bootique.ModuleCrate;
import io.bootique.di.BQModule;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.jersey.client.HttpTargets;
import io.bootique.linkmove.v3.LinkMoveModule;

import javax.inject.Singleton;

/**
 * @since 3.0
 */
public class LinkMoveRestModule implements BQModule, BQModuleProvider {

    @Override
    public ModuleCrate moduleCrate() {
        return ModuleCrate.of(this)
                .description("Integrates Bootique REST Connector to LinkMove ETL framework, v3")
                .build();
    }

    @Override
    public void configure(Binder binder) {
        LinkMoveModule.extend(binder).addConnectorFactory(RestConnectorFactory.class);
    }

    @Singleton
    @Provides
    RestConnectorFactory provideRestConnectorFactory(HttpTargets targets) {
        return new RestConnectorFactory(targets);
    }
}
