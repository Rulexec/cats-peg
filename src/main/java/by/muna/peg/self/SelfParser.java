package by.muna.peg.self;

import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.expressions.ProductExpression;
import by.muna.peg.grammar.expressions.QuantifiedExpression;
import by.muna.peg.grammar.expressions.SquareExpression;
import by.muna.peg.grammar.expressions.square.CharInterval;

import java.util.Arrays;
import java.util.List;

public class SelfParser {
    @SuppressWarnings("unchecked")
    public static final PEGExpression UINT = new ProductExpression(
        Arrays.asList(
            new SquareExpression(
                Arrays.<Character>asList(),
                Arrays.asList(new CharInterval('1', '9')),
                false
            ),
            new QuantifiedExpression(
                new SquareExpression(
                    Arrays.<Character>asList(),
                    Arrays.asList(new CharInterval('1', '9')),
                    false
                ),
                0
            )
        ),
        parsing -> SelfParser.uintFromCharAndCharsList(
            (Character) parsing.get(0), (List<Character>) parsing.get(1)
        )
    );

    private static int uintFromCharAndCharsList(char c, List<Character> chars) {
        int result = c - '0';

        for (char ch : chars) {
            result *= 10;
            result += ch - '0';
        }

        return result;
    }
}
