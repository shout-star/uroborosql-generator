package jp.co.future.uroborosql.generator.config;

import java.util.Properties;

public class EntityConfig {
    private Properties props;

    public EntityConfig(Properties props) {
        this.props = props;
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getPackageName() {
        return props.getProperty("package.name");
    }

    public String getAuthorName() {
        return props.getProperty("author.name");
    }

    public String getBaseModelName() {
        return props.getProperty("base.model.name");
    }

    public String getLockVersionName() {
        return props.getProperty("lock.version.name");
    }

}
