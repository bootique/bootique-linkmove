/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.bootique.linkmove.cayenne41;

import io.bootique.config.ConfigurationFactory;
import io.bootique.linkmove.cayenne41.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.cayenne41.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.cayenne41.connector.URIConnectorFactoryFactory;
import io.bootique.resource.ResourceFactory;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinkMoveModule_ConnectorsConfigIT {

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

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/cayenne41/config.yml");

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
    public void testConfiguration_ConnectorFactories_Classpath() {

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/cayenne41/connectors.yml");


        assertEquals("Unexpected number of connector factory providers", 1, f.getConnectorFactories().size());

        IConnectorFactoryFactory<?> factory;

        factory = f.getConnectorFactories().get(0);
        assertTrue(factory instanceof URIConnectorFactoryFactory);

        ResourceFactory resourceFactory = ((URIConnectorFactoryFactory) factory).getConnectors().get("domainSourceConnector");
        File file = new File(resourceFactory.getUrl().getFile());

        assertTrue(file.getAbsolutePath() + " is not a file", file.isFile());
    }
}
