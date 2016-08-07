package io.bootique.linkmove.connector;

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.jdbc.DataSourceFactory;

import javax.sql.DataSource;

//TODO: remove this hack once https://github.com/nhl/link-move/issues/71 
//is in (or LM is switched to Java 8)
public class Java8DataSourceConnectorFactory implements IConnectorFactory<JdbcConnector> {

	private DataSourceFactory dataSourceFactory;

	public Java8DataSourceConnectorFactory(DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	@Override
	public JdbcConnector createConnector(String id) {
		return new Java8DataSourceConnector(id, connectorDataSource(id));
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
