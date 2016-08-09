package io.bootique.linkmove;

import com.nhl.link.move.runtime.LmRuntimeBuilder;

public interface LinkMoveRuntimeBuildCallback {
	void build(LmRuntimeBuilder builder);
}
