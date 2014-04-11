package by.muna.peg.self.model;

import java.util.List;
import java.util.Map;

public class SyntaxModel {
    private Map<String, List<IDirectiveModel>> directives;
    private List<RuleModel> rules;

    public SyntaxModel(List<RuleModel> rules, Map<String, List<IDirectiveModel>> directives) {
        this.rules = rules;
        this.directives = directives;
    }

    public List<RuleModel> getRules() {
        return rules;
    }

    public Map<String, List<IDirectiveModel>> getDirectives() {
        return directives;
    }
}
