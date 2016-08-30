package io.bootique.linkmove;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import io.bootique.config.ConfigurationFactory;
import io.bootique.config.jackson.JsonNodeConfigurationFactory;
import io.bootique.jackson.DefaultJacksonService;
import io.bootique.jackson.JacksonService;
import io.bootique.linkmove.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.connector.URIConnectorFactoryFactory;
import io.bootique.log.DefaultBootLogger;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkMoveModule_ConfigurationIT {

    private static final String CONFIG_PREFIX = "linkmove";

    private ConfigurationFactory factory(URL configUrl) {

        // not using a mock; making sure all Jackson extensions are loaded
        JacksonService jacksonService = new DefaultJacksonService(new DefaultBootLogger(true));

        YAMLParser parser;
        try {
            parser = new YAMLFactory().createParser(configUrl);
            JsonNode rootNode = jacksonService.newObjectMapper().readTree(parser);
            return new JsonNodeConfigurationFactory(rootNode, jacksonService.newObjectMapper());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConfiguration_ConnectorFactories() {

        LinkMoveFactory f = factory(this.getClass().getResource("/io/bootique/linkmove/config.yml"))
                .config(LinkMoveFactory.class, CONFIG_PREFIX);

        assertEquals("Unexpected number of connector factory providers", 2, f.getConnectorFactories().size());

        IConnectorFactoryFactory<?> factory;

        factory = f.getConnectorFactories().get(0);
        assertTrue(factory instanceof JdbcConnectorFactoryFactory);

        factory = f.getConnectorFactories().get(1);
        assertTrue(factory instanceof URIConnectorFactoryFactory);
        Map<String, String> connectors = ((URIConnectorFactoryFactory) factory).getConnectors();
        assertContainsEntry(connectors, "c1", "file:///path/to/file");
        assertContainsEntry(connectors, "c2", "http://host/path/to/resource");
    }

    private static void assertContainsEntry(Map<?, ?> m, Object k, Object v) {
        assertTrue("Missing key: " + k, m.containsKey(k));
        assertEquals("Unexpected value for key: " + k, m.get(k), v);
    }
}
