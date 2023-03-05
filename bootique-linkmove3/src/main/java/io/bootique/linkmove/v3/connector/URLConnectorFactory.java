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

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.connect.URLConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;

import java.net.URL;
import java.util.Map;
import java.util.Optional;

/**
 * @since 3.0
 */
class URLConnectorFactory implements IConnectorFactory<StreamConnector> {

    private final Map<String, URL> connectorUrls;

    public URLConnectorFactory(Map<String, URL> connectorUrls) {
        this.connectorUrls = connectorUrls;
    }

    @Override
    public Class<StreamConnector> getConnectorType() {
        return StreamConnector.class;
    }

    @Override
    public Optional<? extends StreamConnector> createConnector(String id) throws LmRuntimeException {
        return Optional.ofNullable(connectorUrls.get(id)).map(URLConnector::of);
    }
}
