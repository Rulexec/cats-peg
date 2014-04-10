package by.muna.peg.grammar.expressions.square;

public class CharInterval {
    private char from, to;

    public CharInterval(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }

    @Override
    public int hashCode() {
        // TODO: This is good hashCode?
        return Character.hashCode(this.from) ^ Character.hashCode(this.to);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CharInterval)) return false;

        CharInterval other = (CharInterval) o;

        return this.from == other.from && this.to == other.to;
    }
}
