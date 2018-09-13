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

package io.bootique.linkmove;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Set;

public class LinkMoveModule extends ConfigModule {

    public LinkMoveModule() {
    }

    public LinkMoveModule(String configPrefix) {
        super(configPrefix);
    }

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link LinkMoveModuleExtender} that can be used to load LinkMove custom extensions.
     * @since 0.14
     */
    public static LinkMoveModuleExtender extend(Binder binder) {
        return new LinkMoveModuleExtender(binder);
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

        return configFactory
                .config(LinkMoveFactory.class, configPrefix)
                .createLinkMove(injector, targetRuntime, buildCallbacks);
    }
}
