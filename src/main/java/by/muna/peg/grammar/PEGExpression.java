package by.muna.peg.grammar;

import by.muna.peg.PEGParsingControl;
import by.muna.peg.grammar.exceptions.PEGParseException;

public interface PEGExpression {
    PEGParseResult parse(PEGParsingControl parsing, CharSequence chars, int offset) throws PEGParseException;
}
