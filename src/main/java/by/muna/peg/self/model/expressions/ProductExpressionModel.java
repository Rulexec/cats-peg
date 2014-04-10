package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;

import java.util.List;

public class ProductExpressionModel implements IExpressionModel {
    private List<IExpressionModel> expressions;
    private String code;

    public ProductExpressionModel(List<IExpressionModel> expressions, String code) {
        this.expressions = expressions;
        this.code = code;
    }

    public List<IExpressionModel> getExpressions() {
        return expressions;
    }

    public String getCode() {
        return code;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.PRODUCT;
    }
}
