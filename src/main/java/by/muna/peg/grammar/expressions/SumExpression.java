package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

import java.util.List;

public class SumExpression implements PEGExpression {
    private List<PEGExpression> expressions;

    public SumExpression(List<PEGExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        PEGParseSyntaxException latest = null;

        for (PEGExpression expression : this.expressions) {
            try {
                return expression.parse(parsing, chars, offset);
            } catch (PEGParseSyntaxException ex) {
                // TODO: somehow we need to union exceptions in one
                latest = ex;
            }
        }

        throw latest;
    }
}
