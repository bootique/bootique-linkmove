package io.bootique.linkmove;

import io.bootique.config.ConfigurationFactory;
import io.bootique.linkmove.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.connector.URIConnectorFactoryFactory;
import io.bootique.resource.FolderResourceFactory;
import io.bootique.resource.ResourceFactory;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkMoveModule_ConfigurationIT {

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    private static void assertContainsEntry(Map<?, ResourceFactory> m, Object k, Object v) {
        assertTrue("Missing key: " + k, m.containsKey(k));
        assertEquals("Unexpected value for key: " + k, m.get(k).getResourceId(), v);
    }

    private LinkMoveFactory factory(String appConfig) {
        return testFactory.app("-c", appConfig)
                .autoLoadModules()
                .createRuntime()
                .getInstance(ConfigurationFactory.class)
                .config(LinkMoveFactory.class, "linkmove");
    }

    @Test
    public void testConfiguration_ConnectorFactories() {

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/config.yml");

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

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/extractorsDirClasspath.yml");


        FolderResourceFactory folderResourceFactory = f.getExtractorsDir();
        File file = new File(folderResourceFactory.getUrl().getFile(), "extractor.xml");

        assertTrue(file.getAbsolutePath() + " is not a file", file.isFile());
    }

    @Test
    public void testConfiguration_ConnectorFactories_Classpath() {

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/connectors.yml");


        assertEquals("Unexpected number of connector factory providers", 1, f.getConnectorFactories().size());

        IConnectorFactoryFactory<?> factory;

        factory = f.getConnectorFactories().get(0);
        assertTrue(factory instanceof URIConnectorFactoryFactory);

        ResourceFactory resourceFactory = ((URIConnectorFactoryFactory) factory).getConnectors().get("domainSourceConnector");
        File file = new File(resourceFactory.getUrl().getFile());

        assertTrue(file.getAbsolutePath() + " is not a file", file.isFile());
    }
}
