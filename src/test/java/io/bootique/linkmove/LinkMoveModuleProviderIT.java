package io.bootique.linkmove;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class LinkMoveModuleProviderIT {

	@Test
	public void testPresentInJar() {
	    BQModuleProviderChecker.testPresentInJar(LinkMoveModuleProvider.class);
	}

	@Test
	public void testMetadata() {
		BQModuleProviderChecker.testMetadata(LinkMoveModuleProvider.class);
	}
}
