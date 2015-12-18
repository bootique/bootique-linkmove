package com.mhl.bootique.linkmove;

import static java.util.stream.Collectors.counting;
import static org.junit.Assert.assertEquals;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import org.junit.Test;

import com.nhl.bootique.BQModuleProvider;
import com.nhl.bootique.linkmove.LinkMoveModuleProvider;

public class LinkMoveModuleProviderIT {

	@Test
	public void testPresentInJar() {
		long c = StreamSupport.stream(ServiceLoader.load(BQModuleProvider.class).spliterator(), false)
				.filter(p -> p instanceof LinkMoveModuleProvider).collect(counting());
		assertEquals("No provider found", 1, c);
	}
}
