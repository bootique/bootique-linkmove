package com.nhl.bootique.linkmove;

import com.google.inject.Module;
import com.nhl.bootique.BQModuleProvider;

public class LinkMoveModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkMoveModule();
	}
}
