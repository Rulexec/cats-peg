package by.muna.peg.tools;

import by.muna.peg.PEGParser;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.interpreter.PEGInterpretativeParser;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.SyntaxModel;

public class DevelopmentParsing {
    public static String parse(String syntax, String text) throws PEGParseException {
        return DevelopmentParsing.parse(syntax, text, "start");
    }
    public static String parse(String syntax, String text, String startRule)
        throws PEGParseException
    {
        return OutputFormatter.format(DevelopmentParsing.parseToObject(
            syntax, text, startRule
        ));
    }

    public static Object parseToObject(String syntax, String text) throws PEGParseException {
        return DevelopmentParsing.parseToObject(syntax, text, "start");
    }
    public static Object parseToObject(String syntax, String text, String startRule)
        throws PEGParseException
    {
        SyntaxModel syntaxModel = new SelfParser().parse(syntax);

        PEGParser<Object> parser = new PEGInterpretativeParser<>(syntaxModel, startRule);

        return parser.parse(text);
    }
}
