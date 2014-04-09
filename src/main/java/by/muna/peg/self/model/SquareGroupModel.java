package by.muna.peg.self.model;

public class SquareGroupModel {
    private boolean negate;
    private SquareVariantsModel variants;

    public SquareGroupModel(SquareVariantsModel variants, boolean negate) {
        this.variants = variants;
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public SquareVariantsModel getVariants() {
        return variants;
    }
}
