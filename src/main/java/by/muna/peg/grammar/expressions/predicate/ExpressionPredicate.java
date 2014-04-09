package by.muna.peg.grammar.expressions.predicate;

import by.muna.peg.IPEGParsing;

@FunctionalInterface
public interface ExpressionPredicate {
    boolean test(IPEGParsing parsing);
}
