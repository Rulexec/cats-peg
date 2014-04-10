package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class NameExpressionModel implements IExpressionModel {
    private String name;

    public NameExpressionModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.NAME;
    }
}
