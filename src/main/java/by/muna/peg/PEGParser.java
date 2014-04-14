package by.muna.peg;

import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;

import java.util.Arrays;

public class PEGParser<T> {
    private PEGExpression expression;

    public PEGParser(PEGExpression expression) {
        this.expression = expression;
    }

    @SuppressWarnings("unchecked")
    public T parse(String text) throws PEGParseException {
        PEGParseResult result = this.expression.parse(
            new PEGParsing(), text, 0
        );

        if (result.getNewOffset() < text.length()) {
            throw new PEGParseSyntaxException(
                Arrays.asList(""),
                text.substring(result.getNewOffset()),
                result.getNewOffset()
            );
        } else {
            return (T) result.getResult();
        }
    }
}
