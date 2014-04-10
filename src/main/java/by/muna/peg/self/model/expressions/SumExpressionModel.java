package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

import java.util.List;

public class SumExpressionModel implements IExpressionModel {
    private List<IExpressionModel> expressions;

    public SumExpressionModel(List<IExpressionModel> expressions) {
        this.expressions = expressions;
    }

    public List<IExpressionModel> getExpressions() {
        return expressions;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.SUM;
    }
}
