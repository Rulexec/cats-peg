package by.muna.peg.interpreter;

import by.muna.java.compiler.MemoryJavaCompiler;
import by.muna.java.compiler.StringJavaFileObject;
import by.muna.peg.IPEGParsing;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.expressions.AnyCharExpression;
import by.muna.peg.grammar.expressions.ExpressionHolder;
import by.muna.peg.grammar.expressions.LiteralExpression;
import by.muna.peg.grammar.expressions.LookaheadExpression;
import by.muna.peg.grammar.expressions.NamedExpression;
import by.muna.peg.grammar.expressions.PredicateExpression;
import by.muna.peg.grammar.expressions.ProductExpression;
import by.muna.peg.grammar.expressions.QuantifiedExpression;
import by.muna.peg.grammar.expressions.SquareExpression;
import by.muna.peg.grammar.expressions.SumExpression;
import by.muna.peg.self.model.DirectiveTypeModel;
import by.muna.peg.self.model.ExpressionTypeModel;
import by.muna.peg.self.model.IDirectiveModel;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.QuantificatorModel;
import by.muna.peg.self.model.RuleModel;
import by.muna.peg.self.model.SquareVariantsModel;
import by.muna.peg.self.model.SyntaxModel;
import by.muna.peg.self.model.directives.LiteralDirectiveModel;
import by.muna.peg.self.model.expressions.LiteralExpressionModel;
import by.muna.peg.self.model.expressions.LookaheadExpressionModel;
import by.muna.peg.self.model.expressions.NameExpressionModel;
import by.muna.peg.self.model.expressions.NamedExpressionModel;
import by.muna.peg.self.model.expressions.PredicateExpressionModel;
import by.muna.peg.self.model.expressions.ProductExpressionModel;
import by.muna.peg.self.model.expressions.QuantifiedExpressionModel;
import by.muna.peg.self.model.expressions.SquareExpressionModel;
import by.muna.peg.self.model.expressions.SumExpressionModel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class PEGInterpreter {
    private static class TypeAndIndex {
        public String typeName;
        public int index;

        public TypeAndIndex(String typeName, int index) {
            this.typeName = typeName;
            this.index = index;
        }
    }

    private Map<String, IExpressionModel> rules = new HashMap<>();
    private Map<String, PEGExpression> result = new HashMap<>();
    private Map<String, ExpressionHolder> holders = new HashMap<>();

    private MemoryJavaCompiler compiler = new MemoryJavaCompiler();

    private int lastCompiledClass = 0;

    private List<String> javaImports = new LinkedList<>();

    public PEGInterpreter(SyntaxModel syntax) {
        List<IDirectiveModel> importDirectives = syntax.getDirectives().get("javaImport");
        if (importDirectives != null) {
            for (IDirectiveModel directive : importDirectives) {
                if (directive.getDirectiveType() != DirectiveTypeModel.LITERAL) {
                    throw new RuntimeException(
                        "javaImport must be literal: " + directive.getDirectiveType()
                    );
                }

                this.javaImports.add(((LiteralDirectiveModel) directive).getLiteral());
            }
        }

        for (RuleModel rule : syntax.getRules()) {
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
            // Normally predicates can be only in product,
            // because in other cases he haven't access to any variables,
            // but if someone put predicate in sum or something like that,
            // we call him without variables.
            // FIXME: Must be empty map
            return this.createPredicate((PredicateExpressionModel) expr, new TreeMap<>());
        }
        case PRODUCT: {
            ProductExpressionModel product = (ProductExpressionModel) expr;

            List<PEGExpression> expressions = new ArrayList<>();
            Map<String, TypeAndIndex> localVars = new HashMap<>();

            int i = 0;
            for (IExpressionModel e : product.getExpressions()) {
                if (e.getExpressionType() == ExpressionTypeModel.NAMED) {
                    NamedExpressionModel named = (NamedExpressionModel) e;

                    String typeName = null;
                    List<IDirectiveModel> typeDirectives = named.getNameDirectives().get("type");
                    if (typeDirectives != null && !typeDirectives.isEmpty()) {
                        typeName = ((LiteralDirectiveModel) typeDirectives.get(0)).getLiteral();
                    } else {
                        typeName = "Object";
                    }

                    localVars.put(named.getName(), new TypeAndIndex(typeName, i));
                }

                i++;

                if (!(e instanceof PredicateExpressionModel)) {
                    expressions.add(this.makePEG(e));
                } else {
                    expressions.add(this.createPredicate(
                        (PredicateExpressionModel) e,
                        localVars
                    ));
                }
            }

            if (product.getCode() != null && !product.getCode().trim().equals("")) {
                Method method = this.createMethod(product.getCode(), localVars, true);

                return new ProductExpression(
                    expressions,
                    parsing -> {
                        try {
                            return method.invoke(null, parsing);
                        } catch (ReflectiveOperationException ex) {
                            // FIXME: must be not runtime exception
                            throw new RuntimeException(ex);
                        }
                    }
                );
            } else {
                return new ProductExpression(expressions);
            }
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

    private PEGExpression createPredicate(
        PredicateExpressionModel predicate,
        Map<String, TypeAndIndex> localVars)
    {
        Method method = this.createMethod(predicate.getCode(), localVars, false);

        return new PredicateExpression(parsing -> {
            try {
                return (Boolean) method.invoke(null, parsing);
            } catch (ReflectiveOperationException ex) {
                // FIXME: must be not runtime exception
                throw new RuntimeException(ex);
            }
        }, predicate.isNegate());
    }

    public PEGExpression getExpression(String ruleName) {
        return this.result.get(ruleName);
    }

    private Method createMethod(String code, Map<String, TypeAndIndex> localVars, boolean isTransform) {
        String packageName = "by.muna.temp.codegen.peg";
        String path = "by/muna/temp/codegen/peg";
        String className = "PEGCode" + (++this.lastCompiledClass);
        String qualifiedClassName = packageName + "." + className;

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(packageName).append(';');
        sb.append("import by.muna.peg.IPEGParsing;");

        for (String imp : this.javaImports) {
            sb.append("import ").append(imp).append(';');
        }

        sb.append("public class ").append(className);
        sb.append("{@SuppressWarnings(\"unchecked\") public static ");
        sb.append(isTransform ? "Object" : "boolean");
        sb.append(" eval(IPEGParsing _____){");

        for (Entry<String, TypeAndIndex> localVar : localVars.entrySet()) {
            TypeAndIndex typeAndIndex = localVar.getValue();
            sb.append(typeAndIndex.typeName);
            sb.append(' ').append(localVar.getKey());
            sb.append("=(").append(typeAndIndex.typeName).append(')');
            sb.append("_____.get(").append(Integer.toString(typeAndIndex.index)).append(");");
        }

        sb.append(code);

        sb.append("}}");

        String javaCode = sb.toString();

        this.compiler.compile(Arrays.asList(new StringJavaFileObject(
            path, className, javaCode
        )));

        try {
            Class cls = this.compiler.loadClass(qualifiedClassName);

            return cls.getMethod("eval", IPEGParsing.class);
        } catch (ReflectiveOperationException ex) {
            // FIXME: It should be not runtime exception.
            throw new RuntimeException(ex);
        }
    }
}
