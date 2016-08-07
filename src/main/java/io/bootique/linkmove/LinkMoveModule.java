package io.bootique.linkmove;

import com.google.inject.Provides;
import com.nhl.link.move.runtime.LmRuntime;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;
import io.bootique.jdbc.DataSourceFactory;
import org.apache.cayenne.configuration.server.ServerRuntime;

public class LinkMoveModule extends ConfigModule {

	public LinkMoveModule() {
	}

	public LinkMoveModule(String configPrefix) {
		super(configPrefix);
	}

	@Provides
	public LmRuntime createLinkMoveRuntime(ConfigurationFactory configFactory,
										   DataSourceFactory dataSourceFactory,
										   ServerRuntime targetRuntime) {

		return configFactory.config(LinkMoveFactory.class, configPrefix).createLinkMove(dataSourceFactory,
				targetRuntime);
	}
}
