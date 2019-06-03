package vlaship.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Parser {

    private final static Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    public static void main(String[] args) {

        LOGGER.info("Robocopy log Parser");

        if (args.length < 2) {
            LOGGER.info("Run java -jar parser.jar input-file output-file");
            return;
        }

        final String inputFile = args[0];
        final String outputFile = args[1];

        LOGGER.info("Input file: {}", inputFile);
        LOGGER.info("Output file: {}", outputFile);

        try {
            final List<String> lines = readFile(inputFile);
            final String parse = parse(lines);
            writeFile(parse, outputFile);
            LOGGER.info("File '{}' is created.", outputFile);

            LOGGER.info("{}{}", "Output\n", parse);

        } catch (IOException | IllegalStateException ex) {
            LOGGER.error("{} {}", ex.getClass().getSimpleName(), ex.getMessage());
        }
    }

    private static void writeFile(final String parse, final String outFile) throws IOException {
        Files.writeString(Paths.get(outFile), parse);
    }

    private static String parse(final List<String> lines) {

        return lines.get(1) + "\n" +
                lines.get(5).substring(2, lines.get(5).length() - 1) + "\n" +
                lines.get(7).substring(2, lines.get(7).length() - 1) + "\n" +
                lines.get(8).substring(2, lines.get(8).length() - 1) + "\n" +
                lines.get(lines.size() - 10).substring(2, lines.get(lines.size() - 10).length() - 1) + "\n" +
                lines.get(lines.size() - 9).substring(2, lines.get(lines.size() - 9).length() - 1) + "\n" +
                lines.get(lines.size() - 8).substring(2, lines.get(lines.size() - 8).length() - 1) + "\n" +
                lines.get(lines.size() - 1).substring(2, lines.get(lines.size() - 1).length() - 1) + "\n" +
                lines.get(1) + "\n" +
                lines.stream().filter(x -> x.contains("ERROR")).reduce((x1, x2) -> x1 + "\n" + x2).orElse("");
    }

    private static List<String> readFile(final String fileName) throws IOException {
        final List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.ISO_8859_1);
        if (lines.isEmpty() || !lines.get(2).contains("ROBOCOPY")) {
            throw new IllegalStateException("Wrong file");
        }
        return lines;
    }

}
