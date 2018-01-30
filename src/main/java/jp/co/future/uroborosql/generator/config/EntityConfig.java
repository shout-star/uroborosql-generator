package jp.co.future.uroborosql.generator.config;

import java.util.Properties;

public class EntityConfig {
	private final Properties props;

	public EntityConfig(final Properties props) {
		this.props = props;
	}

	public String getProperty(final String key) {
		String val = System.getProperty(key);
		if (val != null) {
			return val;
		}
		return this.props.getProperty(key);
	}

	public String getPackageName() {
		return getProperty("package.name");
	}

	public String getOutputDir() {
		return getProperty("output.dir");
	}

	public String getAuthorName() {
		return getProperty("author.name");
	}

	public String getBaseModelName() {
		return getProperty("base.model.name");
	}

	public String getLockVersionName() {
		return getProperty("lock.version.name");
	}

	public String getIncludeTablePattern() {
		return getProperty("include.table.pattern");
	}

	public String getExcludeTablePattern() {
		return getProperty("exclude.table.pattern");
	}

	public String getIncludeColumnPattern() {
		return getProperty("include.column.pattern");
	}

	public String getExcludeColumnPattern() {
		return getProperty("exclude.column.pattern");
	}

	public boolean getEnabledBeanValidation() {
		return "true".equalsIgnoreCase(getProperty("enabled.bean.validation"));
	}

	public String getClassNameSuffix() {
		return getProperty("class.name.suffix");
	}
}
