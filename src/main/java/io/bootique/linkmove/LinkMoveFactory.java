package io.bootique.linkmove;

import com.google.inject.Injector;
import com.nhl.link.move.connect.Connector;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.connector.URIConnectorFactoryFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@BQConfig
public class LinkMoveFactory {

    private String extractorsDir;
    private List<IConnectorFactoryFactory<? extends Connector>> connectorFactories;

    public LinkMoveFactory() {
        this.extractorsDir = ".";
        this.connectorFactories = new ArrayList<>();

        // add default factories
        connectorFactories.add(new JdbcConnectorFactoryFactory());
        connectorFactories.add(new URIConnectorFactoryFactory());
    }

    public LmRuntime createLinkMove(Injector injector, ServerRuntime targetRuntime,
                                    Set<LinkMoveBuilderCallback> builderCallbacks) {

        Objects.requireNonNull(extractorsDir);

        LmRuntimeBuilder builder = new LmRuntimeBuilder().withTargetRuntime(targetRuntime).extractorModelsRoot(extractorsDir);

        connectorFactories.forEach(factory -> addToBuilder(builder, factory, injector));

        builderCallbacks.forEach(c -> c.build(builder));
        return builder.build();
    }

    protected <C extends Connector> void addToBuilder(LmRuntimeBuilder builder, IConnectorFactoryFactory<C> factoryFactory, Injector injector) {
        builder.withConnectorFactory(factoryFactory.getConnectorType(), factoryFactory.getConnectorFactory(injector));
    }

    public List<IConnectorFactoryFactory<? extends Connector>> getConnectorFactories() {
        return connectorFactories;
    }

    @BQConfigProperty
    public void setConnectorFactories(List<IConnectorFactoryFactory<? extends Connector>> connectorFactories) {
        this.connectorFactories = connectorFactories;
    }

    public String getExtractorsDir() {
        return extractorsDir;
    }

    @BQConfigProperty
    public void setExtractorsDir(String extractorsDir) {
        this.extractorsDir = extractorsDir;
    }
}
