package by.muna.peg.self;

import by.muna.peg.PEGParser;
import by.muna.peg.grammar.PEGExpression;
import by.muna.peg.grammar.expressions.AnyCharExpression;
import by.muna.peg.grammar.expressions.ExpressionHolder;
import by.muna.peg.grammar.expressions.LiteralExpression;
import by.muna.peg.grammar.expressions.LookaheadExpression;
import by.muna.peg.grammar.expressions.ProductExpression;
import by.muna.peg.grammar.expressions.QuantifiedExpression;
import by.muna.peg.grammar.expressions.SquareExpression;
import by.muna.peg.grammar.expressions.SumExpression;
import by.muna.peg.grammar.expressions.square.CharInterval;
import by.muna.peg.self.model.DirectiveModel;
import by.muna.peg.self.model.DirectivedNameModel;
import by.muna.peg.self.model.IDirectiveModel;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.NameModel;
import by.muna.peg.self.model.QuantificatorModel;
import by.muna.peg.self.model.RuleModel;
import by.muna.peg.self.model.SquareVariantsModel;
import by.muna.peg.self.model.SyntaxModel;
import by.muna.peg.self.model.directives.LiteralDirectiveModel;
import by.muna.peg.self.model.expressions.AnyCharExpressionModel;
import by.muna.peg.self.model.expressions.LiteralExpressionModel;
import by.muna.peg.self.model.expressions.LookaheadExpressionModel;
import by.muna.peg.self.model.expressions.NameExpressionModel;
import by.muna.peg.self.model.expressions.NamedExpressionModel;
import by.muna.peg.self.model.expressions.PredicateExpressionModel;
import by.muna.peg.self.model.expressions.ProductExpressionModel;
import by.muna.peg.self.model.expressions.QuantifiedExpressionModel;
import by.muna.peg.self.model.expressions.SquareExpressionModel;
import by.muna.peg.self.model.expressions.SumExpressionModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SelfParser extends PEGParser<SyntaxModel> {
    public SelfParser() {
        super(SelfParser.SYNTAX);
    }

    public static final PEGExpression WS = new QuantifiedExpression(
        new SquareExpression(
            Arrays.asList(' ', '\t', '\r', '\n'),
            SelfParser.emptyList()
        ), 1
    );
    public static final PEGExpression WS_MAYBE = SelfParser.quantify(SelfParser.WS, 0, 1);

    @SuppressWarnings("unchecked")
    public static final PEGExpression UINT = new ProductExpression(
        Arrays.asList(
            new SquareExpression(
                Arrays.<Character>asList(),
                Arrays.asList(new CharInterval('1', '9')),
                false
            ),
            new QuantifiedExpression(
                new SquareExpression(
                    Arrays.<Character>asList(),
                    Arrays.asList(new CharInterval('1', '9')),
                    false
                ),
                0
            )
        ),
        parsing -> SelfParser.uintFromCharAndCharsList(
            (Character) parsing.get(0), (List<Character>) parsing.get(1)
        )
    );

    public static final PEGExpression UINT_OR_STAR = new SumExpression(Arrays.asList(
        SelfParser.UINT,
        new LiteralExpression("*")
    ));

    public static final PEGExpression QUANTIFICATOR = new SumExpression(Arrays.asList(
        SelfParser.quantificator(new LiteralExpression("?"), 0, 1),
        SelfParser.quantificator(new LiteralExpression("+"), 1),
        SelfParser.quantificator(new LiteralExpression("*"), 0),

        new ProductExpression(Arrays.asList(
            new LiteralExpression("<"),
            SelfParser.WS_MAYBE,
            SelfParser.UINT,
            SelfParser.WS_MAYBE,
            new LiteralExpression(","),
            SelfParser.WS_MAYBE,
            SelfParser.UINT_OR_STAR,
            SelfParser.WS_MAYBE,
            new LiteralExpression(">")
        ), parsing -> {
            Object uintOrStar = parsing.get(6);

            if (uintOrStar instanceof Integer) {
                return new QuantificatorModel((Integer) parsing.get(2), (Integer) uintOrStar);
            } else {
                return new QuantificatorModel((Integer) parsing.get(2));
            }
        })
    ));
    private static PEGExpression quantify(PEGExpression expr, int from) {
        return new QuantifiedExpression(expr, from);
    }
    private static PEGExpression quantify(PEGExpression expr, int from, int to) {
        return new QuantifiedExpression(expr, from, to);
    }
    private static PEGExpression quantificator(PEGExpression expr, int from) {
        return new ProductExpression(Arrays.asList(expr), parsing -> new QuantificatorModel(from));
    }
    private static PEGExpression quantificator(PEGExpression expr, int from, int to) {
        return new ProductExpression(Arrays.asList(expr), parsing -> new QuantificatorModel(from, to));
    }

    public static final PEGExpression ESCAPED_CHAR = new ProductExpression(
        Arrays.asList(
            new LiteralExpression("\\"),
            new AnyCharExpression()
        ),
        parsing -> {
            char c = (Character) parsing.get(1);

            switch (c) {
            case 'r': return '\r';
            case 'n': return '\n';
            case 't': return '\t';
            default: return c;
            }
        }
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression NAME = new ProductExpression(
        Arrays.asList(
            new SquareExpression(
                SelfParser.emptyList(),
                Arrays.asList(new CharInterval('a', 'z'), new CharInterval('A', 'Z'))
            ),
            SelfParser.quantify(new SquareExpression(
                Arrays.asList('_'),
                Arrays.asList(
                    new CharInterval('a', 'z'),
                    new CharInterval('A', 'Z'),
                    new CharInterval('0', '9')
                )
            ), 0)
        ),
        parsing -> {
            char a = (Character) parsing.get(0);
            List<Character> chars = (List<Character>) parsing.get(1);

            StringBuilder sb = new StringBuilder();

            sb.append(a);
            for (char c: chars) {
                sb.append(c);
            }

            return new NameModel(sb.toString());
        }
    );

    public static final PEGExpression SQUARE_CHAR = new SumExpression(Arrays.asList(
        SelfParser.ESCAPED_CHAR,
        new SquareExpression(Arrays.asList('-', ']'), SelfParser.emptyList(), true)
    ));

    public static final PEGExpression SQUARE_INTERVAL = new ProductExpression(
        Arrays.asList(
            SelfParser.SQUARE_CHAR,
            new LiteralExpression("-"),
            SelfParser.SQUARE_CHAR
        ),
        parsing -> new CharInterval((Character) parsing.get(0), (Character) parsing.get(2))
    );

    public static final PEGExpression SQUARE_VARIANT = new SumExpression(Arrays.asList(
        SelfParser.SQUARE_INTERVAL, SelfParser.SQUARE_CHAR
    ));

    @SuppressWarnings("unchecked")
    public static final PEGExpression SQUARE_VARIANTS = new ProductExpression(
        Arrays.asList(SelfParser.quantify(SelfParser.SQUARE_VARIANT, 1)),
        parsing -> {
            List<Character> chars = new LinkedList<>();
            List<CharInterval> intervals = new LinkedList<>();

            List<Object> variants = (List<Object>) parsing.get(0);

            for (Object variant : variants) {
                if (variant instanceof CharInterval) {
                    intervals.add((CharInterval) variant);
                } else {
                    chars.add((Character) variant);
                }
            }

            return new SquareVariantsModel(chars, intervals);
        }
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression SQUARE_GROUP = new ProductExpression(
        Arrays.asList(
            new LiteralExpression("["),
            SelfParser.quantify(new LiteralExpression("^"), 0, 1),
            SelfParser.SQUARE_VARIANTS,
            new LiteralExpression("]")
        ),
        parsing -> new SquareExpressionModel(
            (SquareVariantsModel) parsing.get(2),
            !((List<Object>) parsing.get(1)).isEmpty()
        )
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression CODE_INNER_CHAR = new SumExpression(Arrays.asList(
        new ProductExpression(
            Arrays.asList(
                SelfParser.quantify(new LiteralExpression("\\"), 1),
                new LiteralExpression("@}")
            ),
            parsing -> {
                StringBuilder sb = new StringBuilder(3);

                List<Character> escapes = (List<Character>) parsing.get(0);

                Iterator<Character> it = escapes.iterator();
                if (it.hasNext()) it.next();

                while (it.hasNext()) sb.append(it.next());

                sb.append("@}");

                return sb.toString();
            }
        ),
        new ProductExpression(
            Arrays.asList(
                new LookaheadExpression(
                    new ProductExpression(Arrays.asList(SelfParser.WS_MAYBE, new LiteralExpression("@}"))),
                    true
                ),
                new AnyCharExpression()
            ),
            parsing -> parsing.get(1).toString()
        )
    ));

    @SuppressWarnings("unchecked")
    public static final PEGExpression CODE = new ProductExpression(
        Arrays.asList(
            new LiteralExpression("{@"),
            SelfParser.WS_MAYBE,
            SelfParser.quantify(SelfParser.CODE_INNER_CHAR, 0),
            SelfParser.WS_MAYBE,
            new LiteralExpression("@}")
        ),
        parsing -> {
            StringBuilder sb = new StringBuilder();

            List<String> strings = (List<String>) parsing.get(2);

            for (String s : strings) {
                sb.append(s);
            }

            return sb.toString();
        }
    );

    public static final PEGExpression LITERAL_CHAR = new SumExpression(Arrays.asList(
        SelfParser.ESCAPED_CHAR,
        new SquareExpression(Arrays.asList('\''), SelfParser.emptyList(), true)
    ));

    @SuppressWarnings("unchecked")
    public static final PEGExpression LITERAL = new ProductExpression(
        Arrays.asList(
            new LiteralExpression("'"),
            SelfParser.quantify(SelfParser.LITERAL_CHAR, 0),
            new LiteralExpression("'")
        ),
        parsing -> {
            List<Character> chars = (List<Character>) parsing.get(1);

            StringBuilder sb = new StringBuilder();

            for (char c : chars) {
                sb.append(c);
            }

            return new LiteralExpressionModel(sb.toString());
        }
    );

    private static final ExpressionHolder EXPRESSION_HOLD = new ExpressionHolder();

    public static final PEGExpression MATCHING = new SumExpression(Arrays.asList(
        new ProductExpression(
            Arrays.asList(new LiteralExpression(".")),
            parsing -> new AnyCharExpressionModel()
        ),
        SelfParser.LITERAL,
        SelfParser.SQUARE_GROUP,
        new ProductExpression(
            Arrays.asList(
                new ProductExpression(
                    Arrays.asList(SelfParser.NAME),
                    parsing -> new NameExpressionModel(((NameModel) parsing.get(0)).getName())
                ),
                new LookaheadExpression(
                    new ProductExpression(Arrays.asList(
                        SelfParser.WS_MAYBE,
                        new LiteralExpression("=")
                    )),
                    true
                )
            ),
            parsing -> parsing.get(0)
        ),
        new ProductExpression(
            Arrays.asList(
                new LiteralExpression("("),
                SelfParser.WS_MAYBE,
                SelfParser.EXPRESSION_HOLD,
                SelfParser.WS_MAYBE,
                new LiteralExpression(")")
            ),
            parsing -> parsing.get(2)
        )
    ));

    public static final PEGExpression DIRECTIVE = new ProductExpression(
        Arrays.asList(
            new LiteralExpression("#"),
            SelfParser.NAME,
            new LiteralExpression(" "),
            SelfParser.LITERAL
        ),
        parsing -> new DirectiveModel(
            ((NameModel) parsing.get(1)).getName(),
            new LiteralDirectiveModel((String) ((LiteralExpressionModel) parsing.get(3)).getLiteral())
        )
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression DIRECTIVES = new ProductExpression(
        Arrays.asList(
            SelfParser.DIRECTIVE,
            SelfParser.quantify(
                new ProductExpression(
                    Arrays.asList(SelfParser.WS, SelfParser.DIRECTIVE),
                    parsing -> parsing.get(1)
                ),
                0
            )
        ),
        parsing -> {
            DirectiveModel d = (DirectiveModel) parsing.get(0);
            List<DirectiveModel> directivesList = (List<DirectiveModel>) parsing.get(1);

            directivesList.add(0, d);

            Map<String, List<IDirectiveModel>> directives = new HashMap<>();

            for (DirectiveModel directive : (List<DirectiveModel>) directivesList) {
                List<IDirectiveModel> list = directives.computeIfAbsent(
                    directive.getName(), s -> new LinkedList<>()
                );

                list.add(directive.getDirective());
            }

            return directives;
        }
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression NAMING = new SumExpression(Arrays.asList(
        new ProductExpression(
            Arrays.asList(
                new LiteralExpression("("),
                SelfParser.WS_MAYBE,
                SelfParser.NAME,
                SelfParser.WS,
                SelfParser.DIRECTIVES,
                SelfParser.WS_MAYBE,
                new LiteralExpression(")")
            ),
            parsing ->new DirectivedNameModel(
                ((NameModel) parsing.get(2)).getName(),
                (Map<String, List<IDirectiveModel>>) parsing.get(4)
            )
        ),
        SelfParser.NAME
    ));

    @SuppressWarnings("unchecked")
    public static final PEGExpression BASIC_EXPRESSION = new SumExpression(Arrays.asList(
        new ProductExpression(
            Arrays.asList(
                SelfParser.quantify(new ProductExpression(
                    Arrays.asList((SelfParser.NAMING), new LiteralExpression(":")),
                    parsing -> parsing.get(0)
                ), 0, 1),
                SelfParser.MATCHING,
                SelfParser.quantify(SelfParser.QUANTIFICATOR, 0, 1)
            ),
            parsing -> {
                List<NameModel> maybeName = (List<NameModel>) parsing.get(0);
                IExpressionModel matching = (IExpressionModel) parsing.get(1);
                List<QuantificatorModel> maybeQuantificator = (List<QuantificatorModel>) parsing.get(2);

                IExpressionModel expression = matching;
                if (!maybeQuantificator.isEmpty()) {
                    expression = new QuantifiedExpressionModel(
                        expression, maybeQuantificator.get(0)
                    );
                }
                if (!maybeName.isEmpty()) {
                    NameModel nameModel = maybeName.get(0);

                    DirectivedNameModel directived;

                    if (nameModel instanceof DirectivedNameModel) {
                        directived = (DirectivedNameModel) nameModel;
                    } else {
                        // FIXME: here should be a empty map
                        directived = new DirectivedNameModel(nameModel.getName(), new TreeMap<>());
                    }

                    expression = new NamedExpressionModel(expression, directived);
                }

                return expression;
            }
        ),
        new ProductExpression(
            Arrays.asList(
                new SquareExpression(Arrays.<Character>asList('!', '&'), SelfParser.emptyList(), false),
                SelfParser.WS_MAYBE,
                SelfParser.MATCHING,
                SelfParser.quantify(SelfParser.QUANTIFICATOR, 0, 1)
            ),
            parsing -> {
                char t = (Character) parsing.get(0);
                IExpressionModel matching = (IExpressionModel) parsing.get(2);
                List<QuantificatorModel> maybeQuantificator = (List<QuantificatorModel>) parsing.get(3);

                IExpressionModel expression = matching;
                if (!maybeQuantificator.isEmpty()) {
                    expression = new QuantifiedExpressionModel(
                        expression, maybeQuantificator.get(0)
                    );
                }
                expression = new LookaheadExpressionModel(expression, t == '!');

                return expression;
            }
        ),
        new ProductExpression(
            Arrays.asList(
                new SquareExpression(Arrays.<Character>asList('!', '&'), SelfParser.emptyList(), false),
                SelfParser.WS_MAYBE,
                SelfParser.CODE
            ),
            parsing -> {
                char t = (Character) parsing.get(0);

                String code = (String) parsing.get(2);

                return new PredicateExpressionModel(code, t == '!');
            }
        )
    ));

    @SuppressWarnings("unchecked")
    public static final PEGExpression EXPRESSION_PRODUCT = new ProductExpression(
        Arrays.asList(
            SelfParser.BASIC_EXPRESSION,
            SelfParser.quantify(new ProductExpression(
                Arrays.asList(SelfParser.WS, SelfParser.BASIC_EXPRESSION),
                parsing -> parsing.get(1)
            ), 0),
            SelfParser.WS_MAYBE,
            SelfParser.quantify(SelfParser.CODE, 0, 1)
        ),
        parsing -> {
            IExpressionModel e = (IExpressionModel) parsing.get(0);
            List<IExpressionModel> exprs = (List<IExpressionModel>) parsing.get(1);
            List<String> codes = (List<String>) parsing.get(3);

            if (!exprs.isEmpty()) {
                exprs.add(0, e);

                return new ProductExpressionModel(exprs, !codes.isEmpty() ? codes.get(0) : null);
            } else {
                if (codes.isEmpty()) {
                    return e;
                } else {
                    return new ProductExpressionModel(Arrays.asList(e), codes.get(0));
                }
            }
        }
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression EXPRESSION = new ProductExpression(
        Arrays.asList(
            SelfParser.EXPRESSION_PRODUCT,
            SelfParser.quantify(new ProductExpression(
                Arrays.asList(
                    SelfParser.WS_MAYBE, new LiteralExpression("/"), SelfParser.WS_MAYBE,
                    SelfParser.EXPRESSION_PRODUCT
                ),
                parsing -> parsing.get(3)
            ), 0)
        ),
        parsing -> {
            IExpressionModel e = (IExpressionModel) parsing.get(0);
            List<IExpressionModel> expressions = (List<IExpressionModel>) parsing.get(1);

            if (!expressions.isEmpty()) {
                expressions.add(0, e);
                return new SumExpressionModel(expressions);
            } else {
                return e;
            }
        }
    );

    static {
        SelfParser.EXPRESSION_HOLD.setExpression(SelfParser.EXPRESSION);
    }

    public static final PEGExpression RULE = new ProductExpression(
        Arrays.asList(
            SelfParser.NAME, SelfParser.WS_MAYBE, new LiteralExpression("="),
            SelfParser.WS_MAYBE, SelfParser.EXPRESSION
        ),
        parsing -> new RuleModel(
            ((NameModel) parsing.get(0)).getName(),
            (IExpressionModel) parsing.get(4)
        )
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression RULES = new ProductExpression(
        Arrays.asList(
            SelfParser.quantify(new ProductExpression(
                Arrays.asList(SelfParser.WS_MAYBE, SelfParser.RULE),
                parsing -> parsing.get(1)
            ), 0)
        ),
        parsing -> parsing.get(0)
    );

    @SuppressWarnings("unchecked")
    public static final PEGExpression SYNTAX = new ProductExpression(
        Arrays.asList(
            SelfParser.WS_MAYBE,
            SelfParser.quantify(new ProductExpression(
                Arrays.asList(SelfParser.DIRECTIVES, SelfParser.WS),
                parsing -> parsing.get(0)
            ), 0),
            SelfParser.RULES,
            SelfParser.WS_MAYBE
        ),
        parsing -> {
            List<Object> maybeDirectives = (List<Object>) parsing.get(1);
            List<RuleModel> rules = (List<RuleModel>) parsing.get(2);

            if (!maybeDirectives.isEmpty()) {
                return new SyntaxModel(rules, (Map<String, List<IDirectiveModel>>) maybeDirectives.get(0));
            } else {
                // FIXME: must be empty map
                return new SyntaxModel(rules, new TreeMap<>());
            }
        }
    );

    private static <T> List<T> emptyList() {
        return Arrays.asList();
    }

    private static int uintFromCharAndCharsList(char c, List<Character> chars) {
        int result = c - '0';

        for (char ch : chars) {
            result *= 10;
            result += ch - '0';
        }

        return result;
    }
}
