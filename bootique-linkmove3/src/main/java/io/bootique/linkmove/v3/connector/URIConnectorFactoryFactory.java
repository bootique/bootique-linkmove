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

package io.bootique.linkmove.v3.connector;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.di.Injector;
import io.bootique.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2.0.B1
 * @deprecated in favor of RestConnectorFactoryFactory from "bootique-linkmove3-rest"
 */
@BQConfig
@Deprecated(since = "3.0")
@JsonTypeName("uri")
public class URIConnectorFactoryFactory implements IConnectorFactoryFactory<StreamConnector> {

    private static final Logger LOGGER = LoggerFactory.getLogger(URIConnectorFactoryFactory.class);

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

        LOGGER.warn("*** URIConnectorFactoryFactory is deprecated in favor of RestConnectorFactoryFactory from \"bootique-linkmove3-rest\"");

        Map<String, URL> connectorUrls = new HashMap<>((int) (connectors.size() / 0.75d) + 1);
        connectors.forEach((id, rf) -> connectorUrls.put(id, rf.getUrl()));

        return new URLConnectorFactory(connectorUrls);
    }

    public Map<String, ResourceFactory> getConnectors() {
        return connectors;
    }
}
