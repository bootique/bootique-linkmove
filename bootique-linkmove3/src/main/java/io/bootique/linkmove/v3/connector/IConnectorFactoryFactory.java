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

package io.bootique.linkmove.v3.connector;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nhl.link.move.connect.Connector;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import io.bootique.annotation.BQConfig;
import io.bootique.config.PolymorphicConfiguration;
import io.bootique.di.Injector;

/**
 * @since 2.0.B1
 */
@BQConfig
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface IConnectorFactoryFactory<C extends Connector> extends PolymorphicConfiguration {

    Class<C> getConnectorType();

    IConnectorFactory<C> getConnectorFactory(Injector injector);
}
