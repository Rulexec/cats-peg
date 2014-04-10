package by.muna.peg.self.model;

public class RuleModel {
    private String name;
    private IExpressionModel expression;

    public RuleModel(String name, IExpressionModel expression) {
        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public IExpressionModel getExpression() {
        return expression;
    }
}
