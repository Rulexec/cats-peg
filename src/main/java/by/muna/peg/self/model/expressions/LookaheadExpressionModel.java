package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class LookaheadExpressionModel implements IExpressionModel {
    private boolean negate;
    private IExpressionModel expression;

    public LookaheadExpressionModel(IExpressionModel expression, boolean negate) {
        this.expression = expression;
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public IExpressionModel getExpression() {
        return expression;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.LOOKAHEAD;
    }
}
