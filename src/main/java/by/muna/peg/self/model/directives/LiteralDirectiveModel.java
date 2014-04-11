package by.muna.peg.self.model.directives;

import by.muna.peg.self.model.DirectiveTypeModel;
import by.muna.peg.self.model.IDirectiveModel;

public class LiteralDirectiveModel implements IDirectiveModel {
    private String literal;

    public LiteralDirectiveModel(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    public DirectiveTypeModel getDirectiveType() {
        return DirectiveTypeModel.LITERAL;
    }
}
