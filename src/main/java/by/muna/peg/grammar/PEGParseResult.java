package by.muna.peg.grammar;

public class PEGParseResult {
    private Object result;
    private int newOffset;

    public PEGParseResult(Object result, int newOffset) {
        this.result = result;
        this.newOffset = newOffset;
    }

    public Object getResult() {
        return this.result;
    }

    public int getNewOffset() {
        return this.newOffset;
    }
}
