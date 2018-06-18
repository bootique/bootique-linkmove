/**
 *  Licensed to ObjectStyle LLC under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ObjectStyle LLC licenses
 *  this file to you under the Apache License, Version 2.0 (the
 *  “License”); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.bootique.linkmove;

import com.google.inject.Injector;
import com.nhl.link.move.connect.Connector;
import com.nhl.link.move.resource.FolderResourceResolver;
import com.nhl.link.move.resource.ResourceResolver;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.connector.URIConnectorFactoryFactory;
import io.bootique.linkmove.resource.BQUrlResourceResolver;
import io.bootique.resource.FolderResourceFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@BQConfig
public class LinkMoveFactory {

    private FolderResourceFactory extractorsDir;
    private List<IConnectorFactoryFactory<? extends Connector>> connectorFactories;

    public LinkMoveFactory() {
        this.extractorsDir = new FolderResourceFactory(".");
        this.connectorFactories = new ArrayList<>();

        // add default factories
        connectorFactories.add(new JdbcConnectorFactoryFactory());
        connectorFactories.add(new URIConnectorFactoryFactory());
    }

    public LmRuntime createLinkMove(
            Injector injector,
            ServerRuntime targetRuntime,
            Set<LinkMoveBuilderCallback> builderCallbacks) {

        ResourceResolver resolver = createResolver();
        LmRuntimeBuilder builder = new LmRuntimeBuilder()
                .withTargetRuntime(targetRuntime)
                .extractorResolver(resolver);

        connectorFactories.forEach(factory -> addToBuilder(builder, factory, injector));

        builderCallbacks.forEach(c -> c.build(builder));
        return builder.build();
    }

    protected ResourceResolver createResolver() {

        Objects.requireNonNull(extractorsDir);

        // handle dir URLs differently - they are reloadable...

        return folderUrl()
                .map(this::crateFolderResolver)
                .orElseGet(() -> new BQUrlResourceResolver(extractorsDir));
    }

    protected ResourceResolver crateFolderResolver(URL url) {
        try {
            return new FolderResourceResolver(new File(url.toURI()));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Not a 'file:' URL: " + url, e);
        }
    }

    protected Optional<URL> folderUrl() {

        if (extractorsDir.getResourceId().startsWith("classpath:")) {
            return Optional.empty();
        }

        URL url = extractorsDir.getUrl();
        return "file".equals(url.getProtocol()) ? Optional.of(url) : Optional.empty();
    }

    protected <C extends Connector> void addToBuilder(
            LmRuntimeBuilder builder,
            IConnectorFactoryFactory<C> factoryFactory,
            Injector injector) {
        builder.withConnectorFactory(factoryFactory.getConnectorType(), factoryFactory.getConnectorFactory(injector));
    }

    public List<IConnectorFactoryFactory<? extends Connector>> getConnectorFactories() {
        return connectorFactories;
    }

    @BQConfigProperty
    public void setConnectorFactories(List<IConnectorFactoryFactory<? extends Connector>> connectorFactories) {
        this.connectorFactories = connectorFactories;
    }

    public FolderResourceFactory getExtractorsDir() {
        return extractorsDir;
    }

    @BQConfigProperty
    public void setExtractorsDir(FolderResourceFactory extractorsDir) {
        this.extractorsDir = extractorsDir;
    }
}
