package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class LiteralExpressionModel implements IExpressionModel {
    private String literal;

    public LiteralExpressionModel(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.LITERAL;
    }
}
