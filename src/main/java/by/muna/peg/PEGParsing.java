package by.muna.peg;

import by.muna.peg.stash.PEGParsingStash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PEGParsing implements PEGParsingControl {
    private List<Object> indexedVars = new ArrayList<>();
    private Map<String, Object> namedVars = new HashMap<>();

    public PEGParsing() {

    }

    @Override
    public Object get(int index) {
        return this.indexedVars.get(index);
    }

    @Override
    public Object get(String name) {
        return this.namedVars.get(name);
    }

    @Override
    public void add(Object value) {
        this.indexedVars.add(value);
    }

    @Override
    public void add(String name, Object value) {
        this.namedVars.put(name, value);
    }

    @Override
    public PEGParsingStash stash() {
        PEGParsingStash stash = new PEGParsingStash(this.indexedVars, this.namedVars);

        this.indexedVars = new ArrayList<>();
        this.namedVars = new HashMap<>();

        return stash;
    }

    @Override
    public void restore(PEGParsingStash stash) {
        this.indexedVars = stash.getIndexedVars();
        this.namedVars = stash.getNamedVars();
    }
}
