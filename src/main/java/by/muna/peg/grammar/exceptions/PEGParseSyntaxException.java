package by.muna.peg.grammar.exceptions;

import java.util.List;

public class PEGParseSyntaxException extends PEGParseException {
    private List<String> expected;
    private String got;
    private int offset;

    public PEGParseSyntaxException(List<String> expected, String got, int offset) {
        this.expected = expected;
        this.got = got;
        this.offset = offset;
    }

    public List<String> getExpected() {
        return expected;
    }

    public String getGot() {
        return got;
    }

    public int getOffset() {
        return offset;
    }
}
