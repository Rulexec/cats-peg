package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class NamedExpressionModel implements IExpressionModel {
    private String name;
    private IExpressionModel expression;

    public NamedExpressionModel(IExpressionModel expression, String name) {
        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public IExpressionModel getExpression() {
        return expression;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.NAMED;
    }
}
