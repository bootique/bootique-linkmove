package io.bootique.linkmove;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;
import io.bootique.cayenne.ServerRuntimeFactory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class LinkMoveModuleProvider implements BQModuleProvider {

	@Override
	public Module module() {
		return new LinkMoveModule();
	}

	@Override
	public Map<String, Type> configs() {
		// TODO: config prefix is hardcoded. Refactor away from ConfigModule, and make provider
		// generate config prefix, reusing it in metadata...
		return Collections.singletonMap("linkmove", LinkMoveFactory.class);
	}

	@Override
	public BQModule.Builder moduleBuilder() {
		return BQModuleProvider.super
				.moduleBuilder()
				.description("Provides integration with LinkMove ETL framework.");
	}
}
