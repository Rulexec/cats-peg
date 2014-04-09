package by.muna.peg.stash;

import java.util.List;
import java.util.Map;

public class PEGParsingStash {
    private List<Object> indexedVars;
    private Map<String, Object> namedVars;

    public PEGParsingStash(List<Object> indexedVars, Map<String, Object> namedVars) {
        this.indexedVars = indexedVars;
        this.namedVars = namedVars;
    }

    public List<Object> getIndexedVars() {
        return indexedVars;
    }

    public Map<String, Object> getNamedVars() {
        return namedVars;
    }
}
