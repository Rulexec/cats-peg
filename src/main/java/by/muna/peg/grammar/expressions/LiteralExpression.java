package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

import java.util.Arrays;

public class LiteralExpression implements PEGExpression {
    private String literal;

    public LiteralExpression(String literal) {
        this.literal = literal;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        wayToFail: {
            wayToSuccess: if (chars.length() >= offset + this.literal.length()) {
                for (int i = 0; i < this.literal.length(); i++) {
                    if (chars.charAt(offset + i) != this.literal.charAt(i)) {
                        break wayToSuccess;
                    }
                }

                break wayToFail;
            }

            throw new PEGParseSyntaxException(
                Arrays.asList('\'' + this.literal + '\''),
                chars.subSequence(
                    offset, Math.min(offset + this.literal.length(), chars.length())
                ).toString(),
                offset
            );
        }

        return new PEGParseResult(this.literal, offset + this.literal.length());
    }
}
