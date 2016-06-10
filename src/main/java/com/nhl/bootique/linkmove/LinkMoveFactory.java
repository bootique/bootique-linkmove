package com.nhl.bootique.linkmove;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

import com.nhl.link.move.extractor.model.ExtractorModelContainer;
import com.nhl.link.move.runtime.extractor.model.BaseExtractorModelLoader;
import com.nhl.link.move.runtime.extractor.model.ClasspathExtractorModelLoader;
import org.apache.cayenne.configuration.server.ServerRuntime;

import com.nhl.bootique.jdbc.DataSourceFactory;
import com.nhl.bootique.linkmove.connector.Java8DataSourceConnectorFactory;
import com.nhl.link.move.runtime.LmRuntime;
import com.nhl.link.move.runtime.LmRuntimeBuilder;
import com.nhl.link.move.runtime.connect.IConnectorFactory;
import com.nhl.link.move.runtime.jdbc.JdbcConnector;
import org.apache.commons.lang.StringUtils;

public class LinkMoveFactory {

	private static final String CLASS_PATH_PROTOCOL = "classpath:";

	private String extractorsDir;

	public LinkMoveFactory() {
		this.extractorsDir = ".";
	}

	public LmRuntime createLinkMove(DataSourceFactory dataSourceFactory, ServerRuntime targetRuntime) {

		IConnectorFactory<JdbcConnector> jdbcCF = createJdbcConnectorFactory(dataSourceFactory);
		Objects.requireNonNull(extractorsDir);

		return instantiateRuntimeBuilder(jdbcCF, targetRuntime).build();
	}

	protected IConnectorFactory<JdbcConnector> createJdbcConnectorFactory(DataSourceFactory dataSourceFactory) {
		// TODO: remove this hack once
		// https://github.com/nhl/link-move/issues/71 is in (or LM is switched
		// to Java 8)
		return new Java8DataSourceConnectorFactory(dataSourceFactory);
	}

	public void setExtractorsDir(String extractorsDir) {
		this.extractorsDir = extractorsDir;
	}

	private LmRuntimeBuilder instantiateRuntimeBuilder(IConnectorFactory<JdbcConnector> jdbcConnectorFactory, ServerRuntime targetRuntime) {
		LmRuntimeBuilder builder = new LmRuntimeBuilder().withTargetRuntime(targetRuntime);
		if (StringUtils.isBlank(extractorsDir)) {
			extractorsDir = ".";
		}
		setupExtractorModels(builder, extractorsDir);
		return builder.withConnectorFactory(JdbcConnector.class, jdbcConnectorFactory);
	}

	private void setupExtractorModels(LmRuntimeBuilder runtimeBuilder, String extractorsPath) {
		if (extractorsPath.startsWith(CLASS_PATH_PROTOCOL)) {
			final String path = formatClassPath(extractorsPath.substring(CLASS_PATH_PROTOCOL.length()));
			runtimeBuilder.extractorModelLoader(new ClasspathExtractorModelLoader() {
				@Override
				protected Reader getXmlSource(String name) throws IOException {
					return super.getXmlSource(path + name);
				}
			});
		} else {
			if (!extractorsPath.endsWith("/")) {
				extractorsPath = extractorsPath + "/";
			}
			URI uri = URI.create(extractorsPath);

			try {
				final URL url = uri.isAbsolute() ? uri.toURL() : (new File(extractorsPath)).toURI().toURL();
				runtimeBuilder.extractorModelLoader(new BaseExtractorModelLoader() {

					@Override
					protected Reader getXmlSource(String name) throws IOException {
						if(!name.endsWith(".xml")) {
							name = name + ".xml";
						}
						URL resource = new URL(url, name);
						return new InputStreamReader(resource.openStream(), "UTF-8");
					}

					@Override
					public boolean needsReload(ExtractorModelContainer extractorModelContainer) {
						return false;
					}
				});
			} catch (MalformedURLException var3) {
				throw new RuntimeException("Bad url", var3);
			}
		}
	}

	private String formatClassPath(String classPath) {
		if (classPath == null) {
			return "";
		}
		if (!classPath.isEmpty()) {
			classPath = classPath.trim();
			if (!classPath.endsWith("/")) {
				classPath += "/";
			}
		}
		return classPath;
	}
}
