package io.bootique.linkmove;

import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.jdbc.DataSourceFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Set;

public class LinkMoveModule extends ConfigModule {

    public LinkMoveModule() {
    }

    public LinkMoveModule(String configPrefix) {
        super(configPrefix);
    }

    public static Multibinder<LinkMoveBuilderCallback> contributeBuildCallback(Binder binder) {
        return Multibinder.newSetBinder(binder, LinkMoveBuilderCallback.class);
    }

    @Override
    public void configure(Binder binder) {
        // init collections
        contributeBuildCallback(binder);
    }

    @Provides
    public LmRuntime createLinkMoveRuntime(ConfigurationFactory configFactory,
                                           DataSourceFactory dataSourceFactory,
                                           ServerRuntime targetRuntime,
                                           Set<LinkMoveBuilderCallback> buildCallbacks) {

        return configFactory
                .config(LinkMoveFactory.class, configPrefix)
                .createLinkMove(dataSourceFactory, targetRuntime, buildCallbacks);
    }
}
