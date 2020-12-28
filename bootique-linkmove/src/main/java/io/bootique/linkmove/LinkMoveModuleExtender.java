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

import io.bootique.ModuleExtender;
import io.bootique.di.Binder;
import io.bootique.di.Key;
import io.bootique.di.SetBuilder;

/**
 * @since 0.14
 */
public class LinkMoveModuleExtender extends ModuleExtender<LinkMoveModuleExtender> {

    private SetBuilder<LinkMoveBuilderCallback> buildCallback;

    public LinkMoveModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkMoveModuleExtender initAllExtensions() {
        contributeBuildCallback();
        return this;
    }

    /**
     * @param callbackKey a DI key pointing to a bound callback service.
     * @return this instance of extender
     * @since 1.0.RC1
     */
    public LinkMoveModuleExtender addLinkMoveBuilderCallback(Key<? extends LinkMoveBuilderCallback> callbackKey) {
        contributeBuildCallback().add(callbackKey);
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(LinkMoveBuilderCallback callback) {
        contributeBuildCallback().addInstance(callback);
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(Class<? extends LinkMoveBuilderCallback> callbackType) {
        contributeBuildCallback().add(callbackType);
        return this;
    }

    protected SetBuilder<LinkMoveBuilderCallback> contributeBuildCallback() {
        return buildCallback != null ? buildCallback : (buildCallback = newSet(LinkMoveBuilderCallback.class));
    }
}
