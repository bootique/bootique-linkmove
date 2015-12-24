package com.nhl.bootique.linkmove;

import org.apache.cayenne.configuration.server.ServerRuntime;

import com.google.inject.Provides;
import com.nhl.bootique.ConfigModule;
import com.nhl.bootique.config.ConfigurationFactory;
import com.nhl.bootique.jdbc.DataSourceFactory;
import com.nhl.link.move.runtime.LmRuntime;

public class LinkMoveModule extends ConfigModule {

	public LinkMoveModule() {
	}

	public LinkMoveModule(String configPrefix) {
		super(configPrefix);
	}

	@Provides
	public LmRuntime createLinkMoveRuntime(ConfigurationFactory configFactory, DataSourceFactory dataSourceFactory,
			ServerRuntime targetRuntime) {

		return configFactory.config(LinkMoveFactory.class, configPrefix).createLinkMove(dataSourceFactory,
				targetRuntime);
	}
}
