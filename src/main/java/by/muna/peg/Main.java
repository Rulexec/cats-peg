package by.muna.peg;

import by.muna.peg.tools.DevelopmentParsing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private void init(String[] args) throws Exception {
        String syntaxPath = args[0];
        String textPath = args[1];
        String startRule = args.length > 2 ? args[2] : "start";

        String syntaxText = Main.readFullFile(syntaxPath);
        String textText = Main.readFullFile(textPath);

        System.out.println(DevelopmentParsing.parse(syntaxText, textText, startRule));
    }

    private static String readFullFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        new Main().init(args);
    }
}
