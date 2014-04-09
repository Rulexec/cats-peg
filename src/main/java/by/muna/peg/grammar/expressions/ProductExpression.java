package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGNamedExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.expressions.transform.ExpressionTransform;
import by.muna.peg.stash.PEGParsingStash;

import java.util.ArrayList;
import java.util.List;

public class ProductExpression implements PEGExpression {
    private List<PEGExpression> expressions;
    private ExpressionTransform transformation = null;

    public ProductExpression(List<PEGExpression> expressions) {
        this.expressions = expressions;
    }
    public ProductExpression(List<PEGExpression> expressions, ExpressionTransform transformation) {
        this.expressions = expressions;
        this.transformation = transformation;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        // FIXME: I don't know how variables logic should work

        PEGParsingStash stash = parsing.stash();

        try {
            List<Object> finalResult = null;
            if (this.transformation == null) finalResult = new ArrayList<>(this.expressions.size());

            for (PEGExpression expression : this.expressions) {
                PEGParseResult result = expression.parse(parsing, chars, offset);

                parsing.add(result.getResult());

                if (this.transformation == null) finalResult.add(result.getResult());

                if (expression instanceof PEGNamedExpression) {
                    String name = ((PEGNamedExpression) expression).getName();
                    parsing.add(name, result.getResult());
                }

                offset = result.getNewOffset();
            }

            if (this.transformation != null) {
                return new PEGParseResult(this.transformation.transform(parsing), offset);
            } else {
                return new PEGParseResult(finalResult, offset);
            }
        } finally {
            parsing.restore(stash);
        }
    }
}
