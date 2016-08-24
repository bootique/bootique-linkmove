package io.bootique.linkmove.connector;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import io.bootique.jdbc.DataSourceFactory;

@JsonTypeName("jdbc")
public class DataSourceConnectorFactoryProvider implements IConnectorFactoryProvider<JdbcConnector> {

    @Override
    public Class<JdbcConnector> getConnectorType() {
        return JdbcConnector.class;
    }

    @Override
    public IConnectorFactory<JdbcConnector> getConnectorFactory(Injector injector) {
        return new DataSourceConnectorFactory(injector.getInstance(DataSourceFactory.class));
    }
}
