package jp.co.future.uroborosql.generator;

import jp.co.future.uroborosql.generator.generator.EntityGenerator;
import jp.co.future.uroborosql.generator.generator.Generator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generator Main class
 *
 * @author Kenichi Hoshi
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) throws ParseException {
        showBanner();

        Options opt = new Options();
        opt.addOption("h", false, "Print help.");
        opt.addOption("e", false, "Generate Entity classes.");

        DefaultParser parser = new DefaultParser();
        CommandLine cl = parser.parse(opt, args);

        Generator generator;
        if (cl.hasOption("e")) {
            LOG.info("Start entity generation.");
            generator = new EntityGenerator();
        } else {
            return;
        }
        // generate
        generator.generate();
        LOG.info("Complete.");
    }

    /**
     * Show banner text.
     */
    private static void showBanner() {
        String banner = null;
        try {
            banner = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("banner.txt").toURI())));
            System.out.println(banner);
        } catch (Exception e) {
            // no op
        }
    }


}
