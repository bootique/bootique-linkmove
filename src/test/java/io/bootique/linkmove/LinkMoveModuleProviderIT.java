package io.bootique.linkmove;

import io.bootique.BQModuleProvider;
import org.junit.Test;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.counting;
import static org.junit.Assert.assertEquals;

public class LinkMoveModuleProviderIT {

	@Test
	public void testPresentInJar() {
		long c = StreamSupport.stream(ServiceLoader.load(BQModuleProvider.class).spliterator(), false)
				.filter(p -> p instanceof LinkMoveModuleProvider).collect(counting());
		assertEquals("No provider found", 1, c);
	}
}
