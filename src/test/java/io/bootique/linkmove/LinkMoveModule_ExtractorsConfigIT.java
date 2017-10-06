package io.bootique.linkmove;

import com.nhl.link.move.extractor.model.ExtractorModel;
import com.nhl.link.move.extractor.model.ExtractorName;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.extractor.model.IExtractorModelService;
import io.bootique.test.junit.BQTestFactory;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LinkMoveModule_ExtractorsConfigIT {

    private static final ExtractorName EXTRACTOR_NAME = ExtractorName.create("extractor.xml", ExtractorModel.DEFAULT_NAME);

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Test
    public void testConfiguration_ExtractorsDir_File() {
        assertExtractorModel("classpath:io/bootique/linkmove/extractorsInFilesystemFolder.yml", "folder");

        // TODO: test change reloading...
    }

    @Test
    public void testConfiguration_ExtractorsDir_Classpath() {
        assertExtractorModel("classpath:io/bootique/linkmove/extractorsInClasspathFolder.yml", "cpfolder");
    }

    @Test
    @Ignore
    public void testConfiguration_ExtractorsDir_Classpath_InJar() {
        assertExtractorModel("classpath:io/bootique/linkmove/extractorsInJar.yml", "injar");
    }

    private void assertExtractorModel(String config, String markerProperty) {
        LmRuntime runtime = runtime(config);

        ExtractorModel model = runtime
                .service(IExtractorModelService.class)
                .get(EXTRACTOR_NAME);

        assertNotNull(model);
        assertEquals(EXTRACTOR_NAME.getName(), model.getName());
        assertEquals("jdbc", model.getType());
        assertEquals("true", model.getProperties().get(markerProperty));
    }

    private LmRuntime runtime(String appConfig) {
        return testFactory.app("-c", appConfig)
                .autoLoadModules()
                .createRuntime()
                .getInstance(LmRuntime.class);
    }
}
