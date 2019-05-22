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

import com.nhl.link.move.extractor.model.ExtractorModel;
import com.nhl.link.move.extractor.model.ExtractorName;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.extractor.model.IExtractorModelService;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LinkMoveModule_ExtractorsConfigIT {

    private static final ExtractorName EXTRACTOR_NAME = ExtractorName.create("extractor.xml", ExtractorModel.DEFAULT_NAME);

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testConfiguration_ExtractorsDir_File() throws IOException, InterruptedException {

        File targetFolder = new File("target/extractors/io/bootique/linkmove/cayenne41/folder");
        File targetFile = new File(targetFolder, "extractor.xml");

        targetFolder.mkdirs();
        targetFile.delete();

        Files.copy(
                Paths.get("src/test/resources/io/bootique/linkmove/cayenne41/folder/extractor1.xml"),
                targetFile.toPath());

        ExtractorModelTester tester = tester("classpath:io/bootique/linkmove/cayenne41/extractorsInFilesystemFolder.yml")
                .assertBasics()
                .assertPropertyPresent("folder1")
                .assertPropertyAbsent("folder2");

        // replace the file and test reloading

        targetFile.delete();
        // must sleep long enough for reload to be detected, as file modification time is rounded to seconds..
        Thread.sleep(1100);
        Files.copy(
                Paths.get("src/test/resources/io/bootique/linkmove/cayenne41/folder/extractor2.xml"),
                targetFile.toPath());

        tester.assertPropertyPresent("folder2").assertPropertyAbsent("folder1");
    }

    @Test
    public void testConfiguration_ExtractorsDir_Classpath() {
        tester("classpath:io/bootique/linkmove/cayenne41/extractorsInClasspathFolder.yml")
                .assertBasics()
                .assertPropertyPresent("cpfolder");
    }

    @Test
    public void testConfiguration_ExtractorsDir_Classpath_InJar() {
        // this works because the test jar is added to classpath in the pom.xml via <additionalClasspathElement>
        tester("classpath:io/bootique/linkmove/cayenne41/extractorsInJar.yml")
                .assertBasics()
                .assertPropertyPresent("injar");
    }


    private ExtractorModelTester tester(String appConfig) {
        LmRuntime runtime = testFactory.app("-c", appConfig)
                .autoLoadModules()
                .createRuntime()
                .getInstance(LmRuntime.class);
        return new ExtractorModelTester(runtime);
    }

    class ExtractorModelTester {
        private LmRuntime runtime;

        ExtractorModelTester(LmRuntime runtime) {
            this.runtime = runtime;
        }

        ExtractorModel getModel() {

            ExtractorModel model = runtime
                    .service(IExtractorModelService.class)
                    .get(EXTRACTOR_NAME);

            assertNotNull(model);
            return model;
        }

        ExtractorModelTester assertBasics() {
            ExtractorModel model = getModel();
            assertEquals(EXTRACTOR_NAME.getName(), model.getName());
            assertEquals("jdbc", model.getType());
            return this;
        }

        ExtractorModelTester assertPropertyPresent(String property) {
            assertNotNull(getModel().getProperties().get(property));
            return this;
        }

        ExtractorModelTester assertPropertyAbsent(String property) {
            assertNull(getModel().getProperties().get(property));
            return this;
        }
    }
}
