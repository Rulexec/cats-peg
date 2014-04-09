package by.muna.peg.grammar.expressions.transform;

import by.muna.peg.IPEGParsing;

@FunctionalInterface
public interface ExpressionTransform {
    Object transform(IPEGParsing parsing);
}
