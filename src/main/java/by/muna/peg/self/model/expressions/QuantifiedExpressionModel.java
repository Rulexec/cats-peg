package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.QuantificatorModel;

public class QuantifiedExpressionModel implements IExpressionModel {
    private IExpressionModel expression;
    private QuantificatorModel quantificator;

    public QuantifiedExpressionModel(IExpressionModel expression, QuantificatorModel quantificator) {
        this.expression = expression;
        this.quantificator = quantificator;
    }

    public IExpressionModel getExpression() {
        return expression;
    }

    public QuantificatorModel getQuantificator() {
        return quantificator;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.QUANTIFIED;
    }
}
