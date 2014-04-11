package by.muna.peg.self.model;

public class DirectiveModel {
    private String name;
    private IDirectiveModel directive;

    public DirectiveModel(String name, IDirectiveModel directive) {
        this.name = name;
        this.directive = directive;
    }

    public String getName() {
        return name;
    }

    public IDirectiveModel getDirective() {
        return directive;
    }
}
