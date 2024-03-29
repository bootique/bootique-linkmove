package io.bootique.linkmove.v3.rest;

import com.nhl.link.move.connect.StreamConnector;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @since 3.0
 */
public class RestConnector implements StreamConnector {

    static final String TEMPLATE_VALUE_MAP_PARAMETER = RestConnector.class.getSimpleName() + ".templateValueMap";

    private final WebTarget target;

    public RestConnector(WebTarget target) {
        this.target = target;
    }

    public static RestConnectorUrlTemplateResolver bindTemplateValues() {
        return new RestConnectorUrlTemplateResolver();
    }

    @Override
    public InputStream getInputStream(Map<String, ?> parameters) throws IOException {
        Response response = resolveTarget(parameters).request().get();

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

    protected WebTarget resolveTarget(Map<String, ?> parameters) {
        Map<String, Object> templateParams = (Map<String, Object>) parameters.get(TEMPLATE_VALUE_MAP_PARAMETER);
        return templateParams != null && !templateParams.isEmpty() ? target.resolveTemplates(templateParams) : target;
    }
}
