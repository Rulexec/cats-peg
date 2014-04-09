package by.muna.peg.self.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.Quantificator;
import by.muna.peg.self.model.SquareInterval;
import by.muna.peg.self.model.SquareVariants;
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

        Quantificator q = (Quantificator) result.getResult();

        Assert.assertEquals(2, q.getFrom());
        Assert.assertEquals(3, q.getTo());
        Assert.assertFalse(q.isToInfinity());

        result = SelfParser.QUANTIFICATOR.parse(
            new PEGParsing(), "<5,*>", 0
        );

        q = (Quantificator) result.getResult();

        Assert.assertEquals(5, q.getFrom());
        Assert.assertTrue(q.isToInfinity());
    }

    private void quantificatorSymbolTest(String symbol, int from, int to, boolean toInfinity)
        throws PEGParseException
    {
        PEGParseResult result = SelfParser.QUANTIFICATOR.parse(
            new PEGParsing(), symbol, 0
        );

        Quantificator q = (Quantificator) result.getResult();

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

        Assert.assertEquals("SomeName", result.getResult());

        try {
            SelfParser.NAME.parse(new PEGParsing(), "_Illegal", 0);
            Assert.fail();
        } catch (PEGParseSyntaxException ex) {}
    }

    @Test
    public void codeTest() throws PEGParseException {
        PEGParseResult result = SelfParser.CODE.parse(
            new PEGParsing(),
            "{@ x @}",
            0
        );

        Assert.assertEquals("x", result.getResult());
    }

    @Test
    public void squareVariantsTest() throws PEGParseException {
        PEGParseResult result = SelfParser.SQUARE_VARIANTS.parse(
            new PEGParsing(), "ab0-9\\--$\\]", 0
        );

        SquareVariants variants = (SquareVariants) result.getResult();

        Set<Character> expectedChars = new HashSet<>(Arrays.<Character>asList('a', 'b', ']'));
        Set<SquareInterval> expectedIntervals = new HashSet<>(Arrays.<SquareInterval>asList(
            new SquareInterval('0', '9'),
            new SquareInterval('-', '$')
        ));

        for (Character c: variants.getChars()) {
            Assert.assertTrue(expectedChars.remove(c));
        }

        Assert.assertEquals(0, expectedChars.size());

        for (SquareInterval interval : variants.getIntervals()) {
            Assert.assertTrue(expectedIntervals.remove(interval));
        }

        Assert.assertEquals(0, expectedIntervals.size());
    }
}
