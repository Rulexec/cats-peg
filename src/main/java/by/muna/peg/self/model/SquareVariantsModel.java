package by.muna.peg.self.model;

import by.muna.peg.grammar.expressions.square.CharInterval;

import java.util.List;

public class SquareVariantsModel {
    private List<Character> chars;
    private List<CharInterval> intervals;

    public SquareVariantsModel(List<Character> chars, List<CharInterval> intervals) {
        this.chars = chars;
        this.intervals = intervals;
    }

    public List<Character> getChars() {
        return chars;
    }

    public List<CharInterval> getIntervals() {
        return intervals;
    }
}
