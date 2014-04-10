package by.muna.peg.self.model.expressions;

import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.SquareVariantsModel;

public class SquareExpressionModel implements IExpressionModel {
    private boolean negate;
    private SquareVariantsModel variants;

    public SquareExpressionModel(SquareVariantsModel variants, boolean negate) {
        this.variants = variants;
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public SquareVariantsModel getVariants() {
        return variants;
    }

    @Override
    public ExpressionTypeModel getExpressionType() {
        return ExpressionTypeModel.SQUARE;
    }
}
