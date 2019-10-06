package io.bootique.linkmove.rest.cayenne41;

import com.nhl.link.move.connect.StreamConnector;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class RestConnector implements StreamConnector {

    private WebTarget target;

    public RestConnector(WebTarget target) {
        this.target = target;
    }

    @Override
    public InputStream getInputStream(Map<String, ?> parameters) throws IOException {
        Response response = target.request().get();

        // presumably redirects are followed automatically, so the only response that is not an error is 200...
        if (response.getStatus() != 200) {
            String body = response.readEntity(String.class);

            // throwing IOException to follow LinkMove expectations for StreamConnector error handling
            throw new IOException("Error reading remote HTTP resource. Status: " +
                    response.getStatus() +
                    ", Message: " + body);
        }

        // TODO: do we need to worry about closing anything explicitly
        //  (assuming downstream consuming code closes the InputStream)?
        return response.readEntity(InputStream.class);
    }
}
