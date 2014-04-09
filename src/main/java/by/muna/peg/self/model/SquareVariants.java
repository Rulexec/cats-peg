package by.muna.peg.self.model;

import java.util.List;

public class SquareVariants {
    private List<Character> chars;
    private List<SquareInterval> intervals;

    public SquareVariants(List<Character> chars, List<SquareInterval> intervals) {
        this.chars = chars;
        this.intervals = intervals;
    }

    public List<Character> getChars() {
        return chars;
    }

    public List<SquareInterval> getIntervals() {
        return intervals;
    }
}
