package io.bootique.linkmove.v3.rest;

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.jersey.client.HttpTargets;

import java.util.Optional;

/**
 * @since 3.0
 */
public class RestConnectorFactory implements IConnectorFactory<StreamConnector> {

    private final HttpTargets httpTargets;

    public RestConnectorFactory(HttpTargets httpTargets) {
        this.httpTargets = httpTargets;
    }

    @Override
    public Class<StreamConnector> getConnectorType() {
        return StreamConnector.class;
    }

    @Override
    public Optional<? extends StreamConnector> createConnector(String id) throws LmRuntimeException {
        return httpTargets.getTargetNames().contains(id)
                ? Optional.of(new RestConnector(httpTargets.newTarget(id)))
                : Optional.empty();
    }
}
