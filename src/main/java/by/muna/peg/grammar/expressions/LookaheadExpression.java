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
        if (!this.negate) {
            // TODO: expected/got
            this.expression.parse(parsing, chars, offset);
            return new PEGParseResult(null, offset);
        } else {
            boolean catched = false;

            try {
                this.expression.parse(parsing, chars, offset);
            } catch (PEGParseSyntaxException ex) {
                catched = true;
            }

            // TODO: expected/got
            if (!catched) throw new PEGParseSyntaxException(null, null, offset);

            return new PEGParseResult(null, offset);
        }
    }
}
