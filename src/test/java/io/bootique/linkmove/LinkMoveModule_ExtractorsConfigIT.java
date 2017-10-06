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

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();
    
    private LmRuntime runtime(String appConfig) {
        return testFactory.app("-c", appConfig)
                .autoLoadModules()
                .createRuntime()
                .getInstance(LmRuntime.class);
    }

    @Test
    public void testConfiguration_ExtractorsDir_File() {

        LmRuntime runtime = runtime("classpath:io/bootique/linkmove/extractorsInFilesystemFolder.yml");

        ExtractorModel model = runtime
                .service(IExtractorModelService.class)
                .get(ExtractorName.create("extractor.xml", ExtractorModel.DEFAULT_NAME));

        assertNotNull(model);
        assertEquals(ExtractorModel.DEFAULT_NAME, model.getName());
        assertEquals("jdbc", model.getType());
    }

    @Test
    public void testConfiguration_ExtractorsDir_Classpath() {

        LmRuntime runtime = runtime("classpath:io/bootique/linkmove/extractorsInClasspathFolder.yml");

        ExtractorModel model = runtime
                .service(IExtractorModelService.class)
                .get(ExtractorName.create("extractor.xml", ExtractorModel.DEFAULT_NAME));

        assertNotNull(model);
        assertEquals(ExtractorModel.DEFAULT_NAME, model.getName());
        assertEquals("jdbc", model.getType());
    }

    @Test
    @Ignore
    public void testConfiguration_ExtractorsDir_Classpath_InJar() {

        LmRuntime runtime = runtime("classpath:io/bootique/linkmove/extractorsInJar.yml");

        ExtractorModel model = runtime
                .service(IExtractorModelService.class)
                .get(ExtractorName.create("extractor.xml", ExtractorModel.DEFAULT_NAME));

        assertNotNull(model);
        assertEquals(ExtractorModel.DEFAULT_NAME, model.getName());
        assertEquals("jdbc", model.getType());
    }
}
