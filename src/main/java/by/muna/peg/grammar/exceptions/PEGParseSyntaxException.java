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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Expected: ");
        if (this.expected != null) {
            boolean isFirst = true;
            for (String s : this.expected) {
                if (isFirst) isFirst = false;
                else sb.append(", ");

                sb.append(s);
            }
        } else {
            sb.append("???");
        }

        sb.append(", got: ");
        sb.append(this.got != null ? this.got : "???");

        sb.append(", offset: ");
        sb.append(Integer.toString(this.offset));

        return sb.toString();
    }
}
