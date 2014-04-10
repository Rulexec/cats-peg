package by.muna.peg.interpreter;

import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.expressions.AnyCharExpression;
import by.muna.peg.grammar.expressions.ExpressionHolder;
import by.muna.peg.grammar.expressions.LiteralExpression;
import by.muna.peg.grammar.expressions.LookaheadExpression;
import by.muna.peg.grammar.expressions.NamedExpression;
import by.muna.peg.grammar.expressions.ProductExpression;
import by.muna.peg.grammar.expressions.QuantifiedExpression;
import by.muna.peg.grammar.expressions.SquareExpression;
import by.muna.peg.grammar.expressions.SumExpression;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.QuantificatorModel;
import by.muna.peg.self.model.RuleModel;
import by.muna.peg.self.model.SquareVariantsModel;
import by.muna.peg.self.model.expressions.LiteralExpressionModel;
import by.muna.peg.self.model.expressions.LookaheadExpressionModel;
import by.muna.peg.self.model.expressions.NameExpressionModel;
import by.muna.peg.self.model.expressions.NamedExpressionModel;
import by.muna.peg.self.model.expressions.ProductExpressionModel;
import by.muna.peg.self.model.expressions.QuantifiedExpressionModel;
import by.muna.peg.self.model.expressions.SquareExpressionModel;
import by.muna.peg.self.model.expressions.SumExpressionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PEGInterpreter {
    private Map<String, IExpressionModel> rules = new HashMap<>();
    private Map<String, PEGExpression> result = new HashMap<>();
    private Map<String, ExpressionHolder> holders = new HashMap<>();

    public PEGInterpreter(List<RuleModel> rules) {
        for (RuleModel rule : rules) {
            this.rules.put(rule.getName(), rule.getExpression());
        }

        // TODO: we creating many unnecessary holders, we can build tree and use holder only in bad case.
        for (Entry<String, IExpressionModel> rule : this.rules.entrySet()) {
            this.result.put(rule.getKey(), this.makePEG(rule.getValue()));
        }

        for (Entry<String, ExpressionHolder> holderEntry : this.holders.entrySet()) {
            PEGExpression pegExpression = this.result.get(holderEntry.getKey());

            // FIXME: It should be not runtime exception
            if (pegExpression == null) throw new RuntimeException("Unknown rule: " + holderEntry.getKey());

            holderEntry.getValue().setExpression(pegExpression);
        }
    }

    private PEGExpression getExpressionOrHolder(String name) {
        PEGExpression r = this.result.get(name);

        if (r != null) return r;

        r = this.holders.get(name);

        if (r != null) return r;

        ExpressionHolder holder = new ExpressionHolder();

        this.holders.put(name, holder);

        return holder;
    }

    private PEGExpression makePEG(IExpressionModel expr) {
        switch (expr.getExpressionType()) {
        case ANY_CHAR: return new AnyCharExpression();
        case LITERAL:
            return new LiteralExpression(((LiteralExpressionModel) expr).getLiteral());
        case LOOKAHEAD: {
            LookaheadExpressionModel lookahead = (LookaheadExpressionModel) expr;

            return new LookaheadExpression(this.makePEG(lookahead.getExpression()), lookahead.isNegate());
        }
        case NAME:
            return this.getExpressionOrHolder(((NameExpressionModel) expr).getName());
        case NAMED: {
            NamedExpressionModel named = (NamedExpressionModel) expr;

            return new NamedExpression(this.makePEG(named.getExpression()), named.getName());
        }
        case PREDICATE: {
            //PredicateExpressionModel predicate = (PredicateExpressionModel) expr;

            // TODO: implement. Here we need to eval java-code
            throw new RuntimeException("Predicates not yet supported.");
        }
        case PRODUCT: {
            ProductExpressionModel product = (ProductExpressionModel) expr;

            if (product.getCode() != null && !product.getCode().trim().equals("")) {
                // TODO: implement. Here we need to eval java-code
                throw new RuntimeException("Transformation code not yet supported.");
            }

            List<PEGExpression> expressions = new ArrayList<>();

            for (IExpressionModel e : product.getExpressions()) {
                expressions.add(this.makePEG(e));
            }

            return new ProductExpression(expressions);
        }
        case SUM: {
            SumExpressionModel sum = (SumExpressionModel) expr;

            List<PEGExpression> expressions = new ArrayList<>();

            for (IExpressionModel e : sum.getExpressions()) {
                expressions.add(this.makePEG(e));
            }

            return new SumExpression(expressions);
        }
        case QUANTIFIED: {
            QuantifiedExpressionModel quantified = (QuantifiedExpressionModel) expr;
            QuantificatorModel quantificator = quantified.getQuantificator();

            if (quantificator.isToInfinity()) {
                return new QuantifiedExpression(
                    this.makePEG(quantified.getExpression()), quantificator.getFrom()
                );
            } else {
                return new QuantifiedExpression(
                    this.makePEG(quantified.getExpression()),
                    quantificator.getFrom(), quantificator.getTo()
                );
            }
        }
        case SQUARE: {
            SquareExpressionModel square = (SquareExpressionModel) expr;
            SquareVariantsModel variants = square.getVariants();

            return new SquareExpression(variants.getChars(), variants.getIntervals(), square.isNegate());
        }
        default: throw new RuntimeException("Impossible: " + expr.getExpressionType());
        }
    }

    public PEGExpression getExpression(String ruleName) {
        return this.result.get(ruleName);
    }
}
