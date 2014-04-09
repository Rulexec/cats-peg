package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

public class LookaheadExpression implements PEGExpression {
    private boolean negate;
    private PEGExpression expression;

    public LookaheadExpression(PEGExpression expression, boolean negate) {
        this.expression = expression;
        this.negate = negate;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        try {
            PEGParseResult result = this.expression.parse(parsing, chars, offset);

            // TODO: expected/got
            // TODO: we are in try block, so our exception will be rethrowed with redundant negate-check
            if (negate) throw new PEGParseSyntaxException(null, null, offset);

            return new PEGParseResult(null, offset);
        } catch (PEGParseException ex) {
            if (!this.negate) {
                throw ex;
            } else {
                return new PEGParseResult(null, offset);
            }
        }
    }
}
