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

import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.ModuleExtender;
import io.bootique.di.Binder;
import io.bootique.di.Key;
import io.bootique.di.SetBuilder;
import io.bootique.di.TypeLiteral;

/**
 * @since 2.0
 */
public class LinkMoveModuleExtender extends ModuleExtender<LinkMoveModuleExtender> {

    private static final Key<IConnectorFactory<?>> FACTORY_TYPE_KEY = Key.get(new TypeLiteral<>() {
    });

    private SetBuilder<IConnectorFactory<?>> connectorFactories;
    private SetBuilder<LinkMoveBuilderCallback> builderCallbacks;

    public LinkMoveModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkMoveModuleExtender initAllExtensions() {
        contributeConnectorFactories();
        contributeBuilderCallbacks();
        return this;
    }

    /**
     * @since 3.0
     */
    public LinkMoveModuleExtender addConnectorFactory(IConnectorFactory<?> factory) {
        contributeConnectorFactories().addInstance(factory);
        return this;
    }

    /**
     * @since 3.0
     */
    public LinkMoveModuleExtender addConnectorFactory(Class<? extends IConnectorFactory<?>> factoryType) {
        contributeConnectorFactories().add(factoryType);
        return this;
    }

    /**
     * @param callbackKey a DI key pointing to a bound callback service.
     * @return this instance of extender
     */
    public LinkMoveModuleExtender addLinkMoveBuilderCallback(Key<? extends LinkMoveBuilderCallback> callbackKey) {
        contributeBuilderCallbacks().add(callbackKey);
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(LinkMoveBuilderCallback callback) {
        contributeBuilderCallbacks().addInstance(callback);
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(Class<? extends LinkMoveBuilderCallback> callbackType) {
        contributeBuilderCallbacks().add(callbackType);
        return this;
    }

    protected SetBuilder<IConnectorFactory<?>> contributeConnectorFactories() {
        return connectorFactories != null ? connectorFactories : (connectorFactories = newSet(FACTORY_TYPE_KEY));
    }

    protected SetBuilder<LinkMoveBuilderCallback> contributeBuilderCallbacks() {
        return builderCallbacks != null ? builderCallbacks : (builderCallbacks = newSet(LinkMoveBuilderCallback.class));
    }
}
