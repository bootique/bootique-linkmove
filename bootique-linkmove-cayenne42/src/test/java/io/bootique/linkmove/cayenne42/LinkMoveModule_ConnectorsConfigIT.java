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

package io.bootique.linkmove.cayenne42;

import io.bootique.config.ConfigurationFactory;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestFactory;
import io.bootique.junit5.BQTestTool;
import io.bootique.linkmove.cayenne42.connector.IConnectorFactoryFactory;
import io.bootique.linkmove.cayenne42.connector.JdbcConnectorFactoryFactory;
import io.bootique.linkmove.cayenne42.connector.URIConnectorFactoryFactory;
import io.bootique.resource.ResourceFactory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class LinkMoveModule_ConnectorsConfigIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private static void assertContainsEntry(Map<?, ResourceFactory> m, Object k, Object v) {
        assertTrue(m.containsKey(k), () -> "Missing key: " + k);
        assertEquals(m.get(k).getResourceId(), v, () -> "Unexpected value for key: " + k);
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

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/cayenne42/config.yml");

        assertEquals(2, f.getConnectorFactories().size(), "Unexpected number of connector factory providers");

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

        LinkMoveFactory f = factory("classpath:io/bootique/linkmove/cayenne42/connectors.yml");


        assertEquals(1, f.getConnectorFactories().size(), "Unexpected number of connector factory providers");

        IConnectorFactoryFactory<?> factory;

        factory = f.getConnectorFactories().get(0);
        assertTrue(factory instanceof URIConnectorFactoryFactory);

        ResourceFactory resourceFactory = ((URIConnectorFactoryFactory) factory).getConnectors().get("domainSourceConnector");
        File file = new File(resourceFactory.getUrl().getFile());

        assertTrue(file.isFile(), () -> file.getAbsolutePath() + " is not a file");
    }
}
