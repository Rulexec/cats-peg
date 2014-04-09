package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;

public class ExpressionHolder implements PEGExpression {
    private PEGExpression expression = null;

    public ExpressionHolder() {}

    public void setExpression(PEGExpression expression) {
        if (this.expression != null) throw new RuntimeException("Already set in.");

        this.expression = expression;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        return this.expression.parse(parsing, chars, offset);
    }
}
