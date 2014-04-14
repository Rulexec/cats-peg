package by.muna.peg.interpreter;

import by.muna.peg.PEGParser;
import by.muna.peg.self.model.SyntaxModel;

public class PEGInterpretativeParser<T> extends PEGParser<T> {
    public PEGInterpretativeParser(SyntaxModel syntaxModel, String rule) {
        super(new PEGInterpreter(syntaxModel).getExpression(rule));
    }
}
