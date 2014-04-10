package by.muna.peg.interpreter.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.interpreter.PEGInterpreter;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.RuleModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PEGInterpreterTest {
    @Test
    @SuppressWarnings("unchecked")
    public void arithmeticTest() throws PEGParseException {
        String syntax =
            "start = additive\n" +
            "additive = multiplicative '+' additive / multiplicative\n" +
            "multiplicative = primary '*' multiplicative / primary\n" +
            "primary = integer / '(' additive ')'\n" +
            "integer = [0-9]+";

        PEGParseResult result = SelfParser.RULES.parse(new PEGParsing(), syntax, 0);

        List<RuleModel> rules = (List<RuleModel>) result.getResult();

        PEGInterpreter interpreter = new PEGInterpreter(rules);

        PEGExpression expressions = interpreter.getExpression("start");

        result = expressions.parse(new PEGParsing(), "1+2*42", 0);

        List<Object> addition = (List<Object>) result.getResult();
        Assert.assertEquals(3, addition.size());
        Assert.assertEquals("+", addition.get(1));

        List<Object> numberDigits = (List<Object>) addition.get(0);
        Assert.assertEquals(1, numberDigits.size());

        Assert.assertEquals('1', numberDigits.get(0));

        List<Object> multiplication = (List<Object>) addition.get(2);
        Assert.assertEquals(3, multiplication.size());
        Assert.assertEquals("*", multiplication.get(1));

        numberDigits = (List<Object>) multiplication.get(0);
        Assert.assertEquals(1, numberDigits.size());
        Assert.assertEquals('2', numberDigits.get(0));

        numberDigits = (List<Object>) multiplication.get(2);
        Assert.assertEquals(2, numberDigits.size());
        Assert.assertEquals('4', numberDigits.get(0));
        Assert.assertEquals('2', numberDigits.get(1));
    }
}
