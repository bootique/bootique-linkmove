package io.bootique.linkmove;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import io.bootique.config.ConfigurationFactory;
import io.bootique.config.PolymorphicConfiguration;
import io.bootique.config.TypesFactory;
import io.bootique.config.jackson.JsonNodeConfigurationFactory;
import io.bootique.jackson.DefaultJacksonService;
import io.bootique.jackson.JacksonService;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.connector.URIConnectorFactoryFactory;
import io.bootique.log.DefaultBootLogger;
import io.bootique.resource.FolderResourceFactory;
import io.bootique.resource.ResourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkMoveModule_ConfigurationIT {

    private static final String CONFIG_PREFIX = "linkmove";

    private TypesFactory<PolymorphicConfiguration> typesFactory;

    @Before
    public void before() {
        typesFactory = new TypesFactory<>(
                getClass().getClassLoader(),
                PolymorphicConfiguration.class,
                new DefaultBootLogger(true));
    }

    private ConfigurationFactory factory(URL configUrl) {

        // not using a mock; making sure all Jackson extensions are loaded
        JacksonService jacksonService = new DefaultJacksonService(typesFactory.getTypes());

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
        Map<String, ResourceFactory> connectors = ((URIConnectorFactoryFactory) factory).getConnectors();
        assertContainsEntry(connectors, "c1", "file:///path/to/file");
        assertContainsEntry(connectors, "c2", "http://host/path/to/resource");
    }

    @Test
    public void testConfiguration_ExtractorsDir_Classpath() {
        LinkMoveFactory f = factory(this.getClass().getResource("/io/bootique/linkmove/extractorsDirClasspath.yml"))
                .config(LinkMoveFactory.class, CONFIG_PREFIX);

        FolderResourceFactory folderResourceFactory = f.getExtractorsDir();
        File file = new File(folderResourceFactory.getUrl().getFile(), "extractor.xml");

        assertTrue(file.getAbsolutePath() + " is not a file", file.isFile());
    }

    @Test
    public void testConfiguration_ConnectorFactories_Classpath() {

        LinkMoveFactory f = factory(this.getClass().getResource("/io/bootique/linkmove/connectors.yml"))
                .config(LinkMoveFactory.class, CONFIG_PREFIX);

        assertEquals("Unexpected number of connector factory providers", 1, f.getConnectorFactories().size());

        IConnectorFactoryFactory<?> factory;

        factory = f.getConnectorFactories().get(0);
        assertTrue(factory instanceof URIConnectorFactoryFactory);

        ResourceFactory resourceFactory = ((URIConnectorFactoryFactory) factory).getConnectors().get("domainSourceConnector");
        File file = new File(resourceFactory.getUrl().getFile());

        assertTrue(file.getAbsolutePath() + " is not a file", file.isFile());
    }

    private static void assertContainsEntry(Map<?, ResourceFactory> m, Object k, Object v) {
        assertTrue("Missing key: " + k, m.containsKey(k));
        assertEquals("Unexpected value for key: " + k, m.get(k).getResourceId(), v);
    }
}
