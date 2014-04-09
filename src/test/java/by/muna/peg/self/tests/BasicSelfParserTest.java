package by.muna.peg.self.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.self.SelfParser;
import org.junit.Assert;
import org.junit.Test;

public class BasicSelfParserTest {
    @Test
    public void uintTest() throws PEGParseException {
        PEGParseResult result = SelfParser.UINT.parse(new PEGParsing(), "1337", 0);

        int r = (Integer) result.getResult();

        Assert.assertEquals(1337, r);
    }
}
