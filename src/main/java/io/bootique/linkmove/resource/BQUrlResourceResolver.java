package io.bootique.linkmove.resource;

import com.nhl.link.move.resource.ResourceResolver;
import io.bootique.resource.FolderResourceFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @since 0.24
 */
public class BQUrlResourceResolver implements ResourceResolver {

    private FolderResourceFactory baseFolder;

    public BQUrlResourceResolver(FolderResourceFactory baseFolder) {
        this.baseFolder = baseFolder;
    }

    @Override
    public Reader reader(String location) {

        URL url = baseFolder.getUrl(location);

        try {
            return new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Error reading from " + url);
        }
    }
}
