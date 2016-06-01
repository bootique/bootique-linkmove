package com.nhl.bootique.linkmove;

import java.util.Objects;

import org.apache.cayenne.configuration.server.ServerRuntime;

import com.nhl.bootique.jdbc.DataSourceFactory;
import com.nhl.bootique.linkmove.connector.Java8DataSourceConnectorFactory;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import org.apache.commons.lang.StringUtils;

public class LinkMoveFactory {

	private String extractorsDir;

	public LinkMoveFactory() {
		this.extractorsDir = ".";
	}

	public LmRuntime createLinkMove(DataSourceFactory dataSourceFactory, ServerRuntime targetRuntime) {

		IConnectorFactory<JdbcConnector> jdbcCF = createJdbcConnectorFactory(dataSourceFactory);
		Objects.requireNonNull(extractorsDir);

		return instantiateRuntimeBuilder(jdbcCF, targetRuntime).build();
	}

	protected IConnectorFactory<JdbcConnector> createJdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
		// TODO: remove this hack once
		// https://github.com/nhl/link-move/issues/71 is in (or LM is switched
		// to Java 8)
		return new Java8DataSourceConnectorFactory(dataSourceFactory);
	}

	public void setExtractorsDir(String extractorsDir) {
		this.extractorsDir = extractorsDir;
	}

	private LmRuntimeBuilder instantiateRuntimeBuilder(IConnectorFactory<JdbcConnector> jdbcConnectorFactory, ServerRuntime targetRuntime) {
		LmRuntimeBuilder builder = new LmRuntimeBuilder().withTargetRuntime(targetRuntime);
		if (StringUtils.isBlank(extractorsDir)) {
			extractorsDir = ".";
		}
		LinkMoveExtractorModelProvider.setupExtractorModels(builder, extractorsDir);
		return builder.withConnectorFactory(JdbcConnector.class, jdbcConnectorFactory);
	}
}
