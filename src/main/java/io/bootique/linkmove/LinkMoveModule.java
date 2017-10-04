package io.bootique.linkmove;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

import java.util.Set;

public class LinkMoveModule extends ConfigModule {

    public LinkMoveModule() {
    }

    public LinkMoveModule(String configPrefix) {
        super(configPrefix);
    }

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return an instance of {@link LinkMoveModuleExtender} that can be used to load LinkMove custom extensions.
     * @since 0.14
     */
    public static LinkMoveModuleExtender extend(Binder binder) {
        return new LinkMoveModuleExtender(binder);
    }

    /**
     * @param binder DI binder passed to the Module that invokes this method.
     * @return returns a {@link Multibinder} for LinkMoveBuilderCallbacks.
     * @since 0.13
     * @deprecated since 0.14 call {@link #extend(Binder)} and then call
     * {@link LinkMoveModuleExtender#addLinkMoveBuilderCallback(Class)} or similar methods.
     */
    @Deprecated
    public static Multibinder<LinkMoveBuilderCallback> contributeBuildCallback(Binder binder) {
        return Multibinder.newSetBinder(binder, LinkMoveBuilderCallback.class);
    }

    @Override
    public void configure(Binder binder) {
        extend(binder).initAllExtensions();
    }

    @Provides
    public LmRuntime createLinkMoveRuntime(ConfigurationFactory configFactory,
                                           Injector injector,
                                           ServerRuntime targetRuntime,
                                           Set<LinkMoveBuilderCallback> buildCallbacks) {

        return configFactory
                .config(LinkMoveFactory.class, configPrefix)
                .createLinkMove(injector, targetRuntime, buildCallbacks);
    }
}
