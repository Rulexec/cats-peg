package by.muna.peg.grammar.expressions;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.grammar.exceptions.PEGParseSyntaxException;
import by.muna.peg.grammar.expressions.square.CharInterval;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SquareExpression implements PEGExpression {
    private boolean negate;
    private Set<Character> chars = new HashSet<>();

    // FIXME: We can reduce complexity from O(N) to O(log N) using tree
    private List<CharInterval> intervals;

    public SquareExpression(List<Character> chars, List<CharInterval> intervals, boolean negate) {
        this.negate = negate;

        for (char c : chars) {
            this.chars.add(c);
        }

        this.intervals = intervals;
    }

    @Override
    public PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset)
        throws PEGParseException
    {
        if (offset < chars.length()) {
            char c = chars.charAt(offset);

            if (this.inSquare(c)) {
                return new PEGParseResult(c, offset + 1);
            }
        }

        // TODO: expected variants
        throw new PEGParseSyntaxException(null, null, offset);
    }

    private boolean inSquare(char c) {
        if (this.chars.contains(c)) return !this.negate;

        for (CharInterval interval : this.intervals) {
            if (interval.getFrom() <= c && c <= interval.getTo()) return !this.negate;
        }

        return this.negate;
    }
}
