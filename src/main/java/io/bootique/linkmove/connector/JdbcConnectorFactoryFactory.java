package io.bootique.linkmove.connector;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.annotation.BQConfig;
import io.bootique.jdbc.DataSourceFactory;

@BQConfig
@JsonTypeName("jdbc")
public class JdbcConnectorFactoryFactory implements IConnectorFactoryFactory<JdbcConnector> {

    @Override
    public Class<JdbcConnector> getConnectorType() {
        return JdbcConnector.class;
    }

    @Override
    public IConnectorFactory<JdbcConnector> getConnectorFactory(Injector injector) {
        return new JdbcConnectorFactory(injector.getInstance(DataSourceFactory.class));
    }
}
