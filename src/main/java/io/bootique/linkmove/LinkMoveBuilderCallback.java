package io.bootique.linkmove;

import com.nhl.link.move.runtime.LmRuntimeBuilder;

/**
 * A custom extension that allows users to customize LinkMove stack during creation.
 *
 * @since 0.12
 */
public interface LinkMoveBuilderCallback {

    void build(LmRuntimeBuilder builder);
}
