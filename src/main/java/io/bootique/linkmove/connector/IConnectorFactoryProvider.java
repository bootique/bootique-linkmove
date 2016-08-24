package io.bootique.linkmove.connector;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.inject.Injector;
import com.nhl.link.move.connect.Connector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.config.PolymorphicConfiguration;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface IConnectorFactoryProvider<C extends Connector> extends PolymorphicConfiguration {

    Class<C> getConnectorType();

    IConnectorFactory<C> getConnectorFactory(Injector injector);
}
