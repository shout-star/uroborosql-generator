package jp.co.future.uroborosql.generator.config;

import java.util.Properties;

public class DbConfig {
    private Properties props;

    public DbConfig(Properties props) {
        this.props = props;
    }

    public String getProperty(String key) {
        String val = System.getProperty(key);
        if (val != null) {
            return val;
        }
        return props.getProperty(key);
    }

    public String getUrl() {
        return getProperty("db.url");
    }

    public String getUser() {
        return getProperty("db.user");
    }

    public String getPassword() {
        return getProperty("db.password");
    }

    public String getSchema() {
    	return getProperty("db.schema");
    }

}
