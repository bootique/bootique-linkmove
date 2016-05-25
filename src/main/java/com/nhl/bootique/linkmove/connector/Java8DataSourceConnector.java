package com.nhl.bootique.linkmove.connector;

import javax.sql.DataSource;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.configuration.server.ServerRuntimeBuilder;
import org.apache.cayenne.di.BeforeScopeEnd;
import org.apache.cayenne.java8.CayenneJava8Module;

import com.nhl.link.move.runtime.jdbc.JdbcConnector;

// TODO: remove this hack once https://github.com/nhl/link-move/issues/71 
// is in (or LM is switched to Java 8)
public class Java8DataSourceConnector implements JdbcConnector {

	private ServerRuntime runtime;
	private ObjectContext sharedContext;

	public Java8DataSourceConnector(String name, DataSource dataSource) {
		this.runtime = ServerRuntimeBuilder.builder(name).addModule(new CayenneJava8Module()).dataSource(dataSource)
				.build();
		this.sharedContext = runtime.newContext();
	}

	@Override
	public ObjectContext sharedContext() {
		return sharedContext;
	}

	@BeforeScopeEnd
	@Override
	public void shutdown() {
		runtime.shutdown();
	}

}
