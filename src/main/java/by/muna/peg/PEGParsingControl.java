package by.muna.peg;

import by.muna.peg.stash.PEGParsingStash;

public interface PEGParsingControl extends IPEGParsing {
    PEGParsingStash stash();
    void restore(PEGParsingStash stash);

    void add(Object value);
    void add(String name, Object value);
}
