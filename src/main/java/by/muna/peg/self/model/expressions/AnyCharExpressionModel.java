package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class AnyCharExpressionModel implements IExpressionModel {
    public AnyCharExpressionModel() {}

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.ANY_CHAR;
    }
}
