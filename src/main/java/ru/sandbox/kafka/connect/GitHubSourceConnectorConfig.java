package ru.sandbox.kafka.connect;

import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

public class GitHubSourceConnectorConfig extends AbstractConfig {
    private static final String TOPIC_CONFIG = "topic";
    private static final String TOPIC_DOC = "Topic to write to";

    private static final String OWNER_CONFIG = "github.owner";
    private static final String OWNER_DOC = "GitHub username of the owner of the repository you'd like to follow";

    private static final String REPO_CONFIG = "github.repo";
    private static final String REPO_DOC = "Name of the repository you'd like to follow";

    private static final String SINCE_CONFIG = "since.timestamp";
    private static final String SINCE_DOC = "Only issues updated at or after this time are returned.\n" +
            "This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.\n" +
            "Defaults to one year ago from current date.";

    private static final String BATCH_SIZE_CONFIG = "batch.size";
    private static final String BATCH_SIZE_DOC = "Number of data points to retrieve at one go. Defaults to 100 (max value)";

    private static final String AUTH_USERNAME_CONFIG = "auth.username";
    private static final String AUTH_USERNAME_DOC = "Optional Username to authenticate API calls";

    private static final String AUTH_PASSWORD_CONFIG = "auth.password";
    private static final String AUTH_PASSWORD_DOC = "GitHub user's password to authenticate API calls";

    public GitHubSourceConnectorConfig(ConfigDef definition, Map<?, ?> originals) {
        super(definition, originals);
    }

    public static ConfigDef config() {
        return new ConfigDef()
                .define(TOPIC_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, TOPIC_DOC)
                .define(OWNER_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, OWNER_DOC)
                .define(REPO_CONFIG, ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, REPO_DOC)
                .define(BATCH_SIZE_CONFIG, ConfigDef.Type.INT, 100, new BatchSizeValidator(), ConfigDef.Importance.LOW, BATCH_SIZE_DOC)
                .define(SINCE_CONFIG, ConfigDef.Type.STRING, ZonedDateTime.now().minusYears(1).toInstant().toString(), new TimestampValidator(), ConfigDef.Importance.HIGH, SINCE_DOC)
                .define(AUTH_USERNAME_CONFIG, ConfigDef.Type.STRING, "", ConfigDef.Importance.HIGH, AUTH_USERNAME_DOC)
                .define(AUTH_PASSWORD_CONFIG, ConfigDef.Type.PASSWORD, "", ConfigDef.Importance.HIGH, AUTH_PASSWORD_DOC);
    }

    public String getOwner() { return this.getString(OWNER_CONFIG); }
    public String getRepo() { return this.getString(REPO_CONFIG); }
    public Integer getBatchSize() { return this.getInt(BATCH_SIZE_CONFIG); }
    public Instant getSince() { return Instant.parse(this.getString(SINCE_CONFIG)); }
    public String getTopic() { return this.getString(TOPIC_CONFIG); }
    public String getUsername() { return this.getString(AUTH_USERNAME_CONFIG); }
    public String getPassword() { return this.getPassword(AUTH_PASSWORD_CONFIG).value(); }

}
