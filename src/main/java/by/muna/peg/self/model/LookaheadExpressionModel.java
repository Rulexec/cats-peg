package by.muna.peg.self.model;

public class LookaheadExpressionModel {
    private boolean negate;
    private Object expression;

    public LookaheadExpressionModel(Object expression, boolean negate) {
        this.expression = expression;
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public Object getExpression() {
        return expression;
    }
}
