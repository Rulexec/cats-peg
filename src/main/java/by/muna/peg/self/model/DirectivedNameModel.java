package by.muna.peg.self.model;

import java.util.List;
import java.util.Map;

public class DirectivedNameModel extends NameModel {
    private Map<String, List<IDirectiveModel>> directives;

    public DirectivedNameModel(String name, Map<String, List<IDirectiveModel>> directives) {
        super(name);
        this.directives = directives;
    }

    public Map<String, List<IDirectiveModel>> getDirectives() {
        return directives;
    }
}
