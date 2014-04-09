package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGNamedExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;

public class NamedExpression implements PEGNamedExpression {
    private String name;
    private PEGExpression expression;

    public NamedExpression(PEGExpression expression, String name) {
        this.expression = expression;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset) throws PEGParseException {
        return this.expression.parse(parsing, chars, offset);
    }
}
