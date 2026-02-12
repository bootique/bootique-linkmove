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

import com.nhl.link.move.resource.FolderResourceResolver;
import com.nhl.link.move.resource.ResourceResolver;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.linkmove.v3.resource.BQUrlResourceResolver;
import io.bootique.resource.FolderResourceFactory;
import jakarta.inject.Inject;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.Set;

/**
 * @since 2.0
 */
@BQConfig
public class LinkMoveFactory {

    private final ServerRuntime targetRuntime;
    private final Set<IConnectorFactory<?>> connectorFactories;
    private final Set<LinkMoveBuilderCallback> buildCallbacks;

    private FolderResourceFactory extractorsDir;

    @Inject
    public LinkMoveFactory(
            ServerRuntime targetRuntime,
            Set<IConnectorFactory<?>> connectorFactories,
            Set<LinkMoveBuilderCallback> buildCallbacks) {
        this.targetRuntime = targetRuntime;
        this.connectorFactories = connectorFactories;
        this.buildCallbacks = buildCallbacks;
    }

    public LmRuntime create() {

        ResourceResolver resolver = createResolver();
        LmRuntimeBuilder builder = LmRuntime.builder()
                .targetRuntime(targetRuntime)
                .extractorResolver(resolver);

        connectorFactories.forEach(builder::connectorFactory);

        buildCallbacks.forEach(c -> c.build(builder));
        return builder.build();
    }

    protected ResourceResolver createResolver() {

        FolderResourceFactory dir = this.extractorsDir != null
                ? this.extractorsDir
                : new FolderResourceFactory(".");

        // handle dir URLs differently - they are reloadable...

        return folderUrl(dir)
                .map(this::crateFolderResolver)
                .orElseGet(() -> new BQUrlResourceResolver(dir));
    }

    protected ResourceResolver crateFolderResolver(URL url) {
        try {
            return new FolderResourceResolver(new File(url.toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Not a 'file:' URL: " + url, e);
        }
    }

    protected Optional<URL> folderUrl(FolderResourceFactory dir) {

        if (dir.getResourceId().startsWith("classpath:")) {
            return Optional.empty();
        }

        URL url = dir.getUrl();
        return "file".equals(url.getProtocol()) ? Optional.of(url) : Optional.empty();
    }

    @BQConfigProperty
    public void setExtractorsDir(FolderResourceFactory extractorsDir) {
        this.extractorsDir = extractorsDir;
    }
}
