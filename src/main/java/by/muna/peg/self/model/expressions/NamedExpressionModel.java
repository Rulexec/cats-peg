package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.DirectivedNameModel;
import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IDirectiveModel;
import by.muna.peg.self.model.IExpressionModel;

import java.util.List;
import java.util.Map;

public class NamedExpressionModel implements IExpressionModel {
    private DirectivedNameModel directivedName;
    private IExpressionModel expression;

    public NamedExpressionModel(IExpressionModel expression, DirectivedNameModel directivedName) {
        this.directivedName = directivedName;
        this.expression = expression;
    }

    public String getName() {
        return directivedName.getName();
    }
    public Map<String, List<IDirectiveModel>> getNameDirectives() {
        return this.directivedName.getDirectives();
    }

    public IExpressionModel getExpression() {
        return expression;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.NAMED;
    }
}
