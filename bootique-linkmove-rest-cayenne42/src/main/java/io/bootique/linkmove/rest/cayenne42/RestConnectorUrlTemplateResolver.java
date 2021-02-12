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
package io.bootique.linkmove.rest.cayenne42;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class to build extractor parameters that will be used to resolve "templates" in the connector URL. JAX RS
 * allows to define "templates" like this in {@link javax.ws.rs.client.WebTarget} - "/abc/{path}/{id}?a={a}" and then
 * substitute them with the real values before use. This class assists in collecting such substitutions and passing
 * them to RestConnector via execution parameters.
 *
 * @since 2.0.B1
 */
public class RestConnectorUrlTemplateResolver {

    private Map<String, Object> templateValues;

    public RestConnectorUrlTemplateResolver() {
        this.templateValues = new HashMap<>();
    }

    // TODO: support various other flavors of "WebTarget#resolveTemplate"

    /**
     * @see javax.ws.rs.client.WebTarget#resolveTemplate(String, Object)
     */
    public RestConnectorUrlTemplateResolver value(String name, Object value) {
        templateValues.put(name, value);
        return this;
    }

    public Map<String, Object> toExtractorParameters() {
        return Collections.singletonMap(RestConnector.TEMPLATE_VALUE_MAP_PARAMETER, templateValues);
    }
}
