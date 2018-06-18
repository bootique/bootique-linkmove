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

package io.bootique.linkmove.connector;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.connect.URIConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.resource.ResourceFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@BQConfig
@JsonTypeName("uri")
public class URIConnectorFactoryFactory implements IConnectorFactoryFactory<StreamConnector> {

    private Map<String, ResourceFactory> connectors;

    public URIConnectorFactoryFactory() {
        this.connectors = Collections.emptyMap();
    }

    @BQConfigProperty
    public void setConnectors(Map<String, ResourceFactory> connectors) {
        this.connectors = connectors;
    }

    @Override
    public Class<StreamConnector> getConnectorType() {
        return StreamConnector.class;
    }

    @Override
    public IConnectorFactory<StreamConnector> getConnectorFactory(Injector injector) {

        Map<String, URI> connectorUris = new HashMap<>((int)(connectors.size() / 0.75d) + 1);
        connectors.forEach((id, uri) -> {
            try {
                connectorUris.put(id, uri.getUrl().toURI());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URI for connector with ID: " + id, e);
            }
        });

        return id -> {
            if (!connectorUris.containsKey(id)) {
                throw new IllegalArgumentException("Unknown connector ID: " + id);
            }
            return new URIConnector(connectorUris.get(id));
        };
    }

    public Map<String, ResourceFactory> getConnectors() {
        return connectors;
    }
}
