package org.billFarber.dmsdk;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.ext.ConfiguredDatabaseClientFactory;
import com.marklogic.client.ext.DatabaseClientConfig;
import com.marklogic.client.ext.DefaultConfiguredDatabaseClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

public class DefaultDatabaseClientCreator implements DatabaseClientCreator {
    private static Logger logger = LoggerFactory.getLogger(DefaultDatabaseClientCreator.class);

    private ConfiguredDatabaseClientFactory configuredDatabaseClientFactory;

	public DefaultDatabaseClientCreator() {
		this.configuredDatabaseClientFactory = new DefaultConfiguredDatabaseClientFactory();
	}

	protected DatabaseClientConfig buildDatabaseClientConfig(MarkLogicProperties marklogicProps) {
        logger.info("MarkLogic Host: " + marklogicProps.getProperty(MarkLogicProperties.CONNECTION_HOST));
        logger.info("MarkLogic Port: " + marklogicProps.getProperty(MarkLogicProperties.CONNECTION_PORT));
		logger.info("MarkLogic Connection Type: " + marklogicProps.getProperty(MarkLogicProperties.CONNECTION_TYPE));
        logger.info("MarkLogic User: " + marklogicProps.getProperty(MarkLogicProperties.CONNECTION_USERNAME));

		DatabaseClientConfig clientConfig = new DatabaseClientConfig();

		String type = marklogicProps.getProperty(MarkLogicProperties.CONNECTION_TYPE);
		if (type != null && type.trim().length() > 0) {
			clientConfig.setConnectionType(DatabaseClient.ConnectionType.valueOf(type.toUpperCase()));
		}

		String database = marklogicProps.getProperty(MarkLogicProperties.QUERY_DATABASE);
		if (database != null && database.trim().length() > 0) {
			clientConfig.setDatabase(database);
		}

		clientConfig.setHost(marklogicProps.getProperty(MarkLogicProperties.CONNECTION_HOST));
		clientConfig.setPort(Integer.parseInt(marklogicProps.getProperty(MarkLogicProperties.CONNECTION_PORT)));
		clientConfig.setUsername(marklogicProps.getProperty(MarkLogicProperties.CONNECTION_USERNAME));
		clientConfig.setPassword(marklogicProps.getProperty(MarkLogicProperties.CONNECTION_PASSWORD));

		return clientConfig;
	}

	/**
	 * This provides a "simple" SSL configuration in that it uses the JVM's default SSLContext and
	 * a "trust everything" hostname verifier. No default TrustManager is configured because in the absence of one,
	 * the JVM's cacerts file will be used.
	 *
	 * @param clientConfig
	 */
	protected void configureSimpleSsl(DatabaseClientConfig clientConfig) {
		try {
			clientConfig.setSslContext(SSLContext.getDefault());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to get default SSLContext: " + e.getMessage(), e);
		}

		clientConfig.setSslHostnameVerifier((hostname, cns, subjectAlts) -> {
		});
	}

	@Override
	public DatabaseClient createDatabaseClient(MarkLogicProperties marklogicProps) {
		DatabaseClientConfig clientConfig = buildDatabaseClientConfig(marklogicProps);
		return configuredDatabaseClientFactory.newDatabaseClient(clientConfig);
	}
}
