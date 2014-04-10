package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

public class PredicateExpressionModel implements IExpressionModel {
    private boolean negate;
    private String code;

    public PredicateExpressionModel(String code, boolean negate) {
        this.negate = negate;
        this.code = code;
    }

    public boolean isNegate() {
        return negate;
    }

    public String getCode() {
        return code;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.PREDICATE;
    }
}
