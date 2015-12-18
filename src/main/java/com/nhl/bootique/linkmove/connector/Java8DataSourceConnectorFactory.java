package com.nhl.bootique.linkmove.connector;

import javax.sql.DataSource;

import com.nhl.bootique.jdbc.DataSourceFactory;
import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;

public class Java8DataSourceConnectorFactory implements IConnectorFactory<JdbcConnector> {
	private DataSourceFactory dataSourceFactory;

	public Java8DataSourceConnectorFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	@Override
	public JdbcConnector createConnector(String id) {
		return new Java8DataSourceConnector(connectorDataSource(id));
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
