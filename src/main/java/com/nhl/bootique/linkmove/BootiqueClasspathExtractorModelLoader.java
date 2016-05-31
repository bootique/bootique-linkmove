package com.nhl.bootique.linkmove;

import com.nhl.link.move.runtime.extractor.model.ClasspathExtractorModelLoader;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Reader;

/**
 * Class-path extractor model loader for bootique.
 *
 * Root class-path is configured in the YAML-file as property 'linkmove:extractorsDir' in format
 * 'classpath:[root-classpath]'
 */
public class BootiqueClasspathExtractorModelLoader extends ClasspathExtractorModelLoader {

    private String rootClassPath;

    public BootiqueClasspathExtractorModelLoader(String rootClassPath) {
        this.rootClassPath = rootClassPath.trim();
        if (StringUtils.isNotBlank(this.rootClassPath)) {
            if (!this.rootClassPath.endsWith("/")) {
                this.rootClassPath += "/";
            }
        }
    }

    @Override
    protected Reader getXmlSource(String name) throws IOException {
        return super.getXmlSource(rootClassPath + name);
    }
}
