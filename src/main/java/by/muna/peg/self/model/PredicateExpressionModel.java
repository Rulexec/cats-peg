package by.muna.peg.self.model;

public class PredicateExpressionModel {
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
}
