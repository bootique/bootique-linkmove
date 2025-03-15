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

package io.bootique.linkmove.v3;

import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Provides;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.linkmove.v3.connector.JdbcConnectorFactory;

import jakarta.inject.Singleton;

/**
 * @since 2.0
 */
public class LinkMoveModule implements BQModule {

    private static final String CONFIG_PREFIX = "linkmove";

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link LinkMoveModuleExtender} that can be used to load LinkMove custom extensions.
     */
    public static LinkMoveModuleExtender extend(Binder binder) {
        return new LinkMoveModuleExtender(binder);
    }

    @Override
    public ModuleCrate crate() {
        return ModuleCrate.of(this)
                .description("Integrates LinkMove ETL framework, v3")
                .config(CONFIG_PREFIX, LinkMoveFactory.class)
                .build();
    }

    @Override
    public void configure(Binder binder) {
        LinkMoveModule.extend(binder)
                .initAllExtensions()

                // default bundled connector factories
                .addConnectorFactory(JdbcConnectorFactory.class);
    }

    @Singleton
    @Provides
    LmRuntime provideLinkMoveRuntime(ConfigurationFactory configFactory) {
        return configFactory.config(LinkMoveFactory.class, CONFIG_PREFIX).create();
    }

    @Singleton
    @Provides
    JdbcConnectorFactory provideJdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
        return new JdbcConnectorFactory(dataSourceFactory);
    }
}
