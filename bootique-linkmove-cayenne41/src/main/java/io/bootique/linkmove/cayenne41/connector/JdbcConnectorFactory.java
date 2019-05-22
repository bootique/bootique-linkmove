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

package io.bootique.linkmove.cayenne41.connector;

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.DataSourceConnector;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.jdbc.DataSourceFactory;

import javax.sql.DataSource;

public class JdbcConnectorFactory implements IConnectorFactory<JdbcConnector> {

	private DataSourceFactory dataSourceFactory;

	public JdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	@Override
	public JdbcConnector createConnector(String id) {
		return new DataSourceConnector(id, connectorDataSource(id));
	}

	DataSource connectorDataSource(String id) {

		DataSource ds = dataSourceFactory.forName(id);

		if (ds == null) {
			throw new LmRuntimeException(
					"Unknown JDBC connector ID: " + id + "; available IDs: " + dataSourceFactory.allNames());
		}

		return ds;
	}

}
