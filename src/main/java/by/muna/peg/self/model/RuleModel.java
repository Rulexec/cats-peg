package by.muna.peg.self.model;

public class RuleModel {
    private String name;
    private Object expression;

    public RuleModel(String name, Object expression) {
        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public Object getExpression() {
        return expression;
    }
}
