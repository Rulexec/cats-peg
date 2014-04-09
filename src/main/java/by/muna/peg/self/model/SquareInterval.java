package by.muna.peg.self.model;

public class SquareInterval {
    private char from, to;

    public SquareInterval(char from, char to) {
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
        if (!(o instanceof SquareInterval)) return false;

        SquareInterval other = (SquareInterval) o;

        return (this.from == other.from) && (this.to == other.to);
    }
}
