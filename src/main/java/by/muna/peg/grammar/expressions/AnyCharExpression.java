package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

import java.util.Arrays;

public class AnyCharExpression implements PEGExpression {
    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        if (offset >= chars.length()) {
            throw new PEGParseSyntaxException(Arrays.asList("."), null, offset);
        } else {
            return new PEGParseResult(chars.charAt(offset), offset + 1);
        }
    }
}
