package io.bootique.linkmove;

import com.google.inject.Module;
import io.bootique.BQModuleProvider;

public class LinkMoveModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkMoveModule();
	}
}
