package by.muna.peg.self.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;
import by.muna.peg.grammar.expressions.square.CharInterval;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.expressions.LiteralExpressionModel;
import by.muna.peg.self.model.NameModel;
import by.muna.peg.self.model.QuantificatorModel;
import by.muna.peg.self.model.SquareVariantsModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BasicSelfParserTest {
    @Test
    public void uintTest() throws PEGParseException {
        PEGParseResult result = SelfParser.UINT.parse(new PEGParsing(), "1337", 0);

        int r = (Integer) result.getResult();

        Assert.assertEquals(1337, r);
    }

    @Test
    public void quantificatorSymbolsTest() throws PEGParseException {
        this.quantificatorSymbolTest("?", 0, 1, false);
        this.quantificatorSymbolTest("*", 0, -1, true);
        this.quantificatorSymbolTest("+", 1, -1, true);
    }

    @Test
    public void quantificatorRangeTest() throws PEGParseException {
        PEGParseResult result = SelfParser.QUANTIFICATOR.parse(
            new PEGParsing(), "<2, 3>", 0
        );

        QuantificatorModel q = (QuantificatorModel) result.getResult();

        Assert.assertEquals(2, q.getFrom());
        Assert.assertEquals(3, q.getTo());
        Assert.assertFalse(q.isToInfinity());

        result = SelfParser.QUANTIFICATOR.parse(
            new PEGParsing(), "<5,*>", 0
        );

        q = (QuantificatorModel) result.getResult();

        Assert.assertEquals(5, q.getFrom());
        Assert.assertTrue(q.isToInfinity());
    }

    private void quantificatorSymbolTest(String symbol, int from, int to, boolean toInfinity)
        throws PEGParseException
    {
        PEGParseResult result = SelfParser.QUANTIFICATOR.parse(
            new PEGParsing(), symbol, 0
        );

        QuantificatorModel q = (QuantificatorModel) result.getResult();

        Assert.assertEquals(from, q.getFrom());

        if (!toInfinity) {
            Assert.assertEquals(to, q.getTo());
        }

        Assert.assertEquals(toInfinity, q.isToInfinity());
    }

    @Test
    public void nameTest() throws PEGParseException {
        PEGParseResult result = SelfParser.NAME.parse(
            new PEGParsing(), "SomeName", 0
        );

        Assert.assertEquals("SomeName", ((NameModel) result.getResult()).getName());

        try {
            SelfParser.NAME.parse(new PEGParsing(), "_Illegal", 0);
            Assert.fail();
        } catch (PEGParseSyntaxException ex) {}
    }

    @Test
    public void codeTest() throws PEGParseException {
        PEGParseResult result = SelfParser.CODE.parse(
            new PEGParsing(),
            "{@ some \\@} code \\\\\\@} escaping @}",
            0
        );

        Assert.assertEquals("some @} code \\\\@} escaping", result.getResult());
    }

    @Test
    public void literalTest() throws PEGParseException {
        PEGParseResult result = SelfParser.LITERAL.parse(
            new PEGParsing(),
            "'some \\'literal\\' \\\\ \\n \\t x'",
            0
        );

        Assert.assertEquals("some 'literal' \\ \n \t x", ((LiteralExpressionModel) result.getResult()).getLiteral());
    }

    @Test
    public void squareVariantsTest() throws PEGParseException {
        PEGParseResult result = SelfParser.SQUARE_VARIANTS.parse(
            new PEGParsing(), "ab0-9\\--$\\]", 0
        );

        SquareVariantsModel variants = (SquareVariantsModel) result.getResult();

        Set<Character> expectedChars = new HashSet<>(Arrays.<Character>asList('a', 'b', ']'));
        Set<CharInterval> expectedIntervals = new HashSet<>(Arrays.<CharInterval>asList(
            new CharInterval('0', '9'),
            new CharInterval('-', '$')
        ));

        for (Character c: variants.getChars()) {
            Assert.assertTrue(expectedChars.remove(c));
        }

        Assert.assertEquals(0, expectedChars.size());

        for (CharInterval interval : variants.getIntervals()) {
            Assert.assertTrue(expectedIntervals.remove(interval));
        }

        Assert.assertEquals(0, expectedIntervals.size());
    }
}
