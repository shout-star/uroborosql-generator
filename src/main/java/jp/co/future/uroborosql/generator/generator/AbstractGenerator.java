package jp.co.future.uroborosql.generator.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import jp.co.future.uroborosql.generator.exception.UroborosqlGeneratorException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * AbstractGenerator
 *
 * @author Kenichi Hoshi
 */
abstract class AbstractGenerator implements Generator {

    /**
     * Load properties.
     *
     * @param filePath file path
     * @return <code>Properties</code>
     */
    Properties loadProperties(String filePath) {
        Properties prop = new Properties();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
            prop.load(new InputStreamReader(is, Charset.forName("UTF-8")));
        } catch (IOException e) {
            throw new UroborosqlGeneratorException(e);
        }
        return prop;
    }

    /**
     * Get <code>Template</code> object.
     *
     * @param templateName template name
     * @return <code>Template</code>
     */
    Template getTemplate(String templateName) {
        Configuration cfg = new Configuration(new Version("2.3.26"));
        cfg.setClassForTemplateLoading(getClass(), "/template");
        Template template;

        try {
            template = cfg.getTemplate(templateName);
        } catch (IOException e) {
            throw new UroborosqlGeneratorException(e);
        }
        return template;
    }
}
