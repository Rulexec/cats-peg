package by.muna.peg.self.model;

public class QuantifiedExpressionModel {
    private Object expr;
    private QuantificatorModel quantificator;

    public QuantifiedExpressionModel(Object expr, QuantificatorModel quantificator) {
        this.expr = expr;
        this.quantificator = quantificator;
    }

    public Object getExpr() {
        return expr;
    }

    public QuantificatorModel getQuantificator() {
        return quantificator;
    }
}
