package by.muna.peg.self.model;

public class SquareGroup {
    private boolean negate;
    private SquareVariants variants;

    public SquareGroup(SquareVariants variants, boolean negate) {
        this.variants = variants;
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public SquareVariants getVariants() {
        return variants;
    }
}
