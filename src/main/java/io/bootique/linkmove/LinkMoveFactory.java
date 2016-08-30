package io.bootique.linkmove;

import com.google.inject.Injector;
import com.nhl.link.move.connect.Connector;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LinkMoveFactory {

	private String extractorsDir;

	private List<IConnectorFactoryFactory<Connector>> connectorFactories;

	public void setExtractorsDir(String extractorsDir) {
		this.extractorsDir = extractorsDir;
	}

	public void setConnectorFactories(List<IConnectorFactoryFactory<Connector>> connectorFactories) {
		this.connectorFactories = connectorFactories;
	}

	public LinkMoveFactory() {
		this.extractorsDir = ".";
	}

	public LmRuntime createLinkMove(Injector injector, ServerRuntime targetRuntime,
									Set<LinkMoveBuilderCallback> builderCallbacks) {

		Objects.requireNonNull(extractorsDir);

		LmRuntimeBuilder builder = new LmRuntimeBuilder().withTargetRuntime(targetRuntime).extractorModelsRoot(extractorsDir);

		connectorFactories.forEach(provider ->
				builder.withConnectorFactory(provider.getConnectorType(), provider.getConnectorFactory(injector)));

		builderCallbacks.forEach(c -> c.build(builder));
		return builder.build();
	}

	public List<IConnectorFactoryFactory<Connector>> getConnectorFactories() {
		return connectorFactories;
	}

	public String getExtractorsDir() {
		return extractorsDir;
	}
}
