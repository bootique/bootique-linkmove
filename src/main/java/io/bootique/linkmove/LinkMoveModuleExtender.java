package io.bootique.linkmove;

import com.google.inject.Binder;
import com.google.inject.multibindings.Multibinder;
import io.bootique.ModuleExtender;

/**
 * @since 0.14
 */
public class LinkMoveModuleExtender extends ModuleExtender<LinkMoveModuleExtender> {

    private Multibinder<LinkMoveBuilderCallback> buildCallback;

    public LinkMoveModuleExtender(Binder binder) {
        super(binder);
    }

    @Override
    public LinkMoveModuleExtender initAllExtensions() {
        contributeBuildCallback();
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(LinkMoveBuilderCallback callback) {
        contributeBuildCallback().addBinding().toInstance(callback);
        return this;
    }

    public LinkMoveModuleExtender addLinkMoveBuilderCallback(Class<? extends LinkMoveBuilderCallback> callbackType) {
        contributeBuildCallback().addBinding().to(callbackType);
        return this;
    }

    protected Multibinder<LinkMoveBuilderCallback> contributeBuildCallback() {
        return buildCallback != null ? buildCallback  : (buildCallback = newSet(LinkMoveBuilderCallback.class));
    }
}
