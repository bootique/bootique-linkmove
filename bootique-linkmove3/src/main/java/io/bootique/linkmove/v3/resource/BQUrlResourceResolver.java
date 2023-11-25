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

package io.bootique.linkmove.v3.resource;

import com.nhl.link.move.LmRuntimeException;
import com.nhl.link.move.resource.ResourceResolver;
import io.bootique.resource.FolderResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @since 2.0
 */
public class BQUrlResourceResolver implements ResourceResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BQUrlResourceResolver.class);

    private FolderResourceFactory baseFolder;

    public BQUrlResourceResolver(FolderResourceFactory baseFolder) {
        this.baseFolder = baseFolder;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resources will be located relative to URL {}", baseFolder.getUrl());
        }
    }

    @Override
    public Reader reader(String location) {

        URL url = baseFolder.getUrl(location);
        LOGGER.debug("Will read resource at URL {}", url);

        try {
            return new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new LmRuntimeException("Error reading resource at URL " + url, e);
        }
    }
}
