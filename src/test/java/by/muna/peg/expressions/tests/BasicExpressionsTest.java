package by.muna.peg.expressions.tests;

import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;
import by.muna.peg.grammar.expressions.LiteralExpression;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class BasicExpressionsTest {
    @Test
    public void literalExpressionTest() throws PEGParseException {
        PEGExpression expression = new LiteralExpression("test literal");

        PEGParseResult result = expression.parse(null, "test literal", 0);

        Assert.assertEquals(12, result.getNewOffset());
        Assert.assertEquals("test literal", result.getResult());

        result = expression.parse(null, "text with test literal, yeah.", 10);

        Assert.assertEquals(22, result.getNewOffset());
        Assert.assertEquals("test literal", result.getResult());

        try {
            expression.parse(null, "really no literal here.", 6);
            Assert.fail();
        } catch (PEGParseSyntaxException ex) {
            Assert.assertEquals(6, ex.getOffset());
            Assert.assertEquals(" no literal ", ex.getGot());
            Assert.assertEquals(Arrays.asList("'test literal'"), ex.getExpected());
        }

        try {
            expression.parse(null, "short", 2);
            Assert.fail();
        } catch (PEGParseSyntaxException ex) {
            Assert.assertEquals(2, ex.getOffset());
            Assert.assertEquals("ort", ex.getGot());
            Assert.assertEquals(Arrays.asList("'test literal'"), ex.getExpected());
        }
    }
}
