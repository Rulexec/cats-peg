package by.muna.peg;

import by.muna.peg.interpreter.PEGInterpretativeParser;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.SyntaxModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private Main() {

    }

    private void init(String[] args) throws Exception {
        String syntaxPath = args[0];
        String textPath = args[1];
        String startRule = args.length > 2 ? args[2] : "start";

        String syntaxText = Main.readFullFile(syntaxPath);
        String textText = Main.readFullFile(textPath);

        SyntaxModel syntaxModel = new SelfParser().parse(syntaxText);

        PEGParser<Object> parser = new PEGInterpretativeParser<>(syntaxModel, startRule);

        Object parsed = parser.parse(textText);

        System.out.println(Main.formatResult(parsed));
    }

    @SuppressWarnings("unchecked")
    private static String formatResult(Object o) {
        if (o instanceof List) {
            List<Object> list = (List<Object>) o;

            StringBuilder sb = new StringBuilder();

            sb.append('[');

            boolean isFirst = true;
            for (Object e : list) {
                if (isFirst) isFirst = false;
                else sb.append(", ");

                sb.append(Main.formatResult(e));
            }

            sb.append(']');

            return sb.toString();
        } else if (o instanceof Character) {
            return '\'' + o.toString() + '\'';
        } else if (o instanceof String) {
            return '"' + o.toString() + '"';
        } else {
            return o.toString();
        }
    }

    private static String readFullFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        new Main().init(args);
    }
}
