package by.muna.peg.self.model;

public class NamedExpressionModel {
    private String name;
    private Object expr;

    public NamedExpressionModel(Object expr, String name) {
        this.name = name;
        this.expr = expr;
    }

    public String getName() {
        return name;
    }

    public Object getExpr() {
        return expr;
    }
}
