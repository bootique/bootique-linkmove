package io.bootique.linkmove.v2.rest;

import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.jersey.client.HttpTargets;

/**
 * @since 2.0
 * @deprecated in favor of LinkMove v3
 */
@Deprecated(since = "3.0", forRemoval = true)
public class RestConnectorFactory implements IConnectorFactory<StreamConnector> {

    private HttpTargets httpTargets;

    public RestConnectorFactory(HttpTargets httpTargets) {
        this.httpTargets = httpTargets;
    }

    @Override
    public StreamConnector createConnector(String id) {
        return new RestConnector(httpTargets.newTarget(id));
    }
}
