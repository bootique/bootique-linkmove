package io.bootique.linkmove;

import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.connect.URIConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.linkmove.connector.DataSourceConnectorFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Objects;
import java.util.Set;

public class LinkMoveFactory {

	private String extractorsDir;

	public LinkMoveFactory() {
		this.extractorsDir = ".";
	}

	public LmRuntime createLinkMove(DataSourceFactory dataSourceFactory, ServerRuntime targetRuntime,
	                                Set<LinkMoveBuilderCallback> builderCallbacks) {

		IConnectorFactory<JdbcConnector> jdbcCF = createJdbcConnectorFactory(dataSourceFactory);
		Objects.requireNonNull(extractorsDir);

		LmRuntimeBuilder builder = new LmRuntimeBuilder().withTargetRuntime(targetRuntime).extractorModelsRoot(extractorsDir)
				.withConnectorFactory(JdbcConnector.class, jdbcCF)
				.withConnectorFactory(StreamConnector.class, URIConnectorFactory.class);
		builderCallbacks.forEach(c -> c.build(builder));
		return builder.build();
	}

	protected IConnectorFactory<JdbcConnector> createJdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
		return new DataSourceConnectorFactory(dataSourceFactory);
	}

	public void setExtractorsDir(String extractorsDir) {
		this.extractorsDir = extractorsDir;
	}
}
