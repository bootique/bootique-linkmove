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

import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.DataSourceConnector;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.jdbc.DataSourceFactory;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @since 2.0.B1
 */
public class JdbcConnectorFactory implements IConnectorFactory<JdbcConnector> {

    private final DataSourceFactory dataSourceFactory;
    private final Set<String> knownNames;

    public JdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
        this.knownNames = new HashSet<>(dataSourceFactory.allNames());
    }

    @Override
    public Class<JdbcConnector> getConnectorType() {
        return JdbcConnector.class;
    }

    @Override
    public Optional<JdbcConnector> createConnector(String id) {
        return connectorDataSource(id).map(ds -> new DataSourceConnector(id, ds));
    }

    Optional<DataSource> connectorDataSource(String id) {
        return knownNames.contains(id) ? Optional.of(dataSourceFactory.forName(id)) : Optional.empty();
    }
}
