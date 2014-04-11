package by.muna.peg.interpreter.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.interpreter.PEGInterpreter;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.SyntaxModel;
import org.junit.Assert;
import org.junit.Test;

public class PEGInterpreterTest {
    @Test
    public void arithmeticTest() throws PEGParseException {
        String syntaxText =
            "#javaImport 'java.util.List'\n" +
            "start = additive\n" +
            "additive = (a #type 'Integer'):multiplicative '+' (b #type 'Integer'):additive {@" +
                "return a + b;" +
            "@} / multiplicative\n" +
            "multiplicative = (a #type 'Integer'):primary '*' (b #type 'Integer'):multiplicative {@" +
                "return a * b;" +
            "@} / primary\n" +
            "primary = integer / '(' a:additive ')' {@" +
                "return a;" +
            "@}\n" +
            "integer = (chars #type 'List<Character>'):[0-9]+ {@" +
                "StringBuilder sb = new StringBuilder();" +
                "for (char c : chars) sb.append(c);" +
                "return Integer.parseUnsignedInt(sb.toString());" +
            "@}";

        PEGParseResult result = SelfParser.SYNTAX.parse(new PEGParsing(), syntaxText, 0);

        SyntaxModel syntax = (SyntaxModel) result.getResult();

        PEGInterpreter interpreter = new PEGInterpreter(syntax);

        PEGExpression expressions = interpreter.getExpression("start");

        result = expressions.parse(new PEGParsing(), "1+2*42", 0);

        int number = (Integer) result.getResult();

        Assert.assertEquals(85, number);
    }

    @Test
    public void predicatesTest() throws PEGParseException {
        String syntaxText =
            "start = '1' (a #type 'Character'):. ! {@ return a == '2'; @} .* / '123'";

        PEGParseResult result = SelfParser.SYNTAX.parse(new PEGParsing(), syntaxText, 0);

        SyntaxModel syntax = (SyntaxModel) result.getResult();

        PEGInterpreter interpreter = new PEGInterpreter(syntax);

        PEGExpression expressions = interpreter.getExpression("start");

        result = expressions.parse(new PEGParsing(), "123", 0);

        if (result.getResult() instanceof String) {
            Assert.assertEquals("123", (String) result.getResult());
        } else {
            Assert.fail(result.getResult().toString());
        }
    }
}
