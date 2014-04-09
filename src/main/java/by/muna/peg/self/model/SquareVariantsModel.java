package by.muna.peg.self.model;

import java.util.List;

public class SquareVariantsModel {
    private List<Character> chars;
    private List<SquareIntervalModel> intervals;

    public SquareVariantsModel(List<Character> chars, List<SquareIntervalModel> intervals) {
        this.chars = chars;
        this.intervals = intervals;
    }

    public List<Character> getChars() {
        return chars;
    }

    public List<SquareIntervalModel> getIntervals() {
        return intervals;
    }
}
