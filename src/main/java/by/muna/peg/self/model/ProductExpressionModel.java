package by.muna.peg.self.model;

import java.util.List;

public class ProductExpressionModel {
    private List<Object> expressions;
    private String code;

    public ProductExpressionModel(List<Object> expressions, String code) {
        this.expressions = expressions;
        this.code = code;
    }

    public List<Object> getExpressions() {
        return expressions;
    }

    public String getCode() {
        return code;
    }
}
