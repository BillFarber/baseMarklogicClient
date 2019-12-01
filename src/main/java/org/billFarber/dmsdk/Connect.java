package org.billFarber.dmsdk;

import com.marklogic.client.DatabaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Connect {
    private static Logger logger = LoggerFactory.getLogger(Connect.class);

    public static void main(String args[]) throws IOException {
        logger.info("Starting kafka-marklogic-source");

        MarkLogicProperties marklogicProps = loadConfigurationFromProperties();
        DatabaseClient client = buildMarklogicClient(marklogicProps);

        return;
    }

    private static MarkLogicProperties loadConfigurationFromProperties() throws IOException {
        MarkLogicProperties marklogicProps = new MarkLogicProperties();
        marklogicProps.load(Connect.class.getResourceAsStream("/first.properties"));
        return marklogicProps;
    }

    private static DatabaseClient buildMarklogicClient(MarkLogicProperties marklogicProps) {
        DatabaseClient databaseClient = new DefaultDatabaseClientCreator().createDatabaseClient(marklogicProps);
        return databaseClient;
    }
}
