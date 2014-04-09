package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class QuantifiedExpression implements PEGExpression {
    private int from, to;
    private boolean toInfinity;
    private PEGExpression expression;

    public QuantifiedExpression(PEGExpression expression, int from) {
        this.expression = expression;
        this.from = from;
        this.toInfinity = true;
    }
    public QuantifiedExpression(PEGExpression expression, int from, int to) {
        this.expression = expression;
        this.from = from;
        this.to = to;
        this.toInfinity = false;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        List<Object> results = new ArrayList<>(this.from);

        int parsed = 0;

        // we can not to do this bool check in every iteration
        for (; this.toInfinity || parsed < this.to; parsed++) {
            try {
                PEGParseResult result = this.expression.parse(parsing, chars, offset);

                results.add(result.getResult());

                offset = result.getNewOffset();
            } catch (PEGParseSyntaxException ex) {
                if (parsed >= this.from) {
                    break;
                } else {
                    throw ex;
                }
            }
        }

        return new PEGParseResult(results, offset);
    }
}
