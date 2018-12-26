package ru.sandbox.kafka.connect;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GitHubSourceConnector extends SourceConnector {

    private GitHubSourceConnectorConfig config = null;

    @Override
    public void start(Map<String, String> map) {
        this.config = new GitHubSourceConnectorConfig(map);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return GitHubSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        ArrayList<Map<String, String>> configs = new ArrayList<>(1);
        configs.add(this.config.originalsStrings());
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return GitHubSourceConnectorConfig.config();
    }

    @Override
    public String version() {
        return VersionUtil.version(this.getClass());
    }
}
