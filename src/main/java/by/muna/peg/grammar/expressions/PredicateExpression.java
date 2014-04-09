package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;
import by.muna.peg.grammar.expressions.predicate.ExpressionPredicate;

public class PredicateExpression implements PEGExpression {
    private boolean negate;
    private ExpressionPredicate predicate;

    public PredicateExpression(ExpressionPredicate predicate, boolean negate) {
        this.predicate = predicate;
        this.negate = negate;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        boolean result = this.predicate.test(parsing);

        if (result ^ this.negate) {
            return new PEGParseResult(null, offset);
        } else {
            // TODO: expected/got
            throw new PEGParseSyntaxException(null, null, offset);
        }
    }
}
