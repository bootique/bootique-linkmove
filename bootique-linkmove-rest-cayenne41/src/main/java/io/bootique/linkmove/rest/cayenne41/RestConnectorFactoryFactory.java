package io.bootique.linkmove.rest.cayenne41;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.inject.Injector;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.annotation.BQConfig;
import io.bootique.jersey.client.HttpTargets;
import io.bootique.linkmove.cayenne41.connector.IConnectorFactoryFactory;

/**
 * @since 1.1
 */
@JsonTypeName("bq.rest")
@BQConfig("Creates a LinkMove connector that resolves connector ids as 'targets' in 'jerseyclient' configurations")
public class RestConnectorFactoryFactory implements IConnectorFactoryFactory<StreamConnector> {

    @Override
    public Class<StreamConnector> getConnectorType() {
        return StreamConnector.class;
    }

    @Override
    public IConnectorFactory<StreamConnector> getConnectorFactory(Injector injector) {
        return new RestConnectorFactory(injector.getInstance(HttpTargets.class));
    }
}
