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

import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.BQModule;
import io.bootique.ModuleCrate;
import io.bootique.config.ConfigurationFactory;
import io.bootique.di.Binder;
import io.bootique.di.Injector;
import io.bootique.di.Provides;
import org.apache.cayenne.configuration.server.ServerRuntime;

import jakarta.inject.Singleton;
import java.util.Set;

/**
 * @since 2.0
 * @deprecated in favor of LinkMove v3
 */
@Deprecated(since = "3.0", forRemoval = true)
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
                .description("Deprecated, can be replaced with 'bootique-linkmove3'.")
                .config(CONFIG_PREFIX, LinkMoveFactory.class)
                .build();
    }

    @Override
    public void configure(Binder binder) {
        extend(binder).initAllExtensions();
    }

    @Singleton
    @Provides
    public LmRuntime createLinkMoveRuntime(
            ConfigurationFactory configFactory,
            Injector injector,
            ServerRuntime targetRuntime,
            Set<LinkMoveBuilderCallback> buildCallbacks) {

        return configFactory.config(LinkMoveFactory.class, CONFIG_PREFIX)
                .createLinkMove(injector, targetRuntime, buildCallbacks);
    }
}
