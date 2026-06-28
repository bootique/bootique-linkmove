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

package io.bootique.linkmove.v4;

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.connect.StreamConnector;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.connect.IConnectorService;
import io.bootique.junit.BQTest;
import io.bootique.junit.BQTestFactory;
import io.bootique.junit.BQTestTool;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BQTest
public class LinkMoveModuleExtender_ConnectorIT {

    @BQTestTool
    final BQTestFactory testFactory = new BQTestFactory();

    private IConnectorService connectors(Consumer<LinkMoveModuleExtender> configurator) {
        return testFactory.app()
                .autoLoadModules()
                .module(b -> configurator.accept(LinkMoveModule.extend(b)))
                .createRuntime()
                .getInstance(LmRuntime.class)
                .service(IConnectorService.class);
    }

    @Test
    public void addResourceConnector() throws IOException {
        IConnectorService connectors = connectors(e ->
                e.addResourceConnector("data", "classpath:io/bootique/linkmove/v4/connector/resource.txt"));

        StreamConnector connector = connectors.getConnector(StreamConnector.class, "data");
        assertNotNull(connector);
        assertEquals("Hello LinkMove", read(connector));
    }

    @Test
    public void addResourceConnector_UnknownId() {
        IConnectorService connectors = connectors(e ->
                e.addResourceConnector("data", "classpath:io/bootique/linkmove/v4/connector/resource.txt"));

        assertThrows(LmRuntimeException.class, () -> connectors.getConnector(StreamConnector.class, "noSuchId"));
    }

    @Test
    public void addConnector() {
        StreamConnector instance = params -> new ByteArrayInputStream("custom data".getBytes(StandardCharsets.UTF_8));
        IConnectorService connectors = connectors(e -> e.addConnector(StreamConnector.class, "custom", instance));

        // a connector instance must be made available under its registered id, as-is
        assertSame(instance, connectors.getConnector(StreamConnector.class, "custom"));
    }

    private static String read(StreamConnector connector) throws IOException {
        try (InputStream in = connector.getInputStream(Map.of())) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }
}
