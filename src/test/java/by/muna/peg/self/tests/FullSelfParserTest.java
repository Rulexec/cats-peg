package by.muna.peg.self.tests;

import by.muna.peg.PEGParsing;
import by.muna.peg.grammar.PEGParseResult;
import by.muna.peg.grammar.exceptions.PEGParseException;
import by.muna.peg.self.SelfParser;
import by.muna.peg.self.model.IExpressionModel;
import by.muna.peg.self.model.expressions.LiteralExpressionModel;
import by.muna.peg.self.model.NameModel;
import by.muna.peg.self.model.expressions.NameExpressionModel;
import by.muna.peg.self.model.expressions.ProductExpressionModel;
import by.muna.peg.self.model.RuleModel;
import by.muna.peg.self.model.expressions.SumExpressionModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FullSelfParserTest {
    @Test
    @SuppressWarnings("unchecked")
    public void singleRuleTest() throws PEGParseException {
        PEGParseResult result = SelfParser.RULES.parse(
            new PEGParsing(), "braces = '(' braces ')' / '()'", 0
        );

        List<RuleModel> rules = (List<RuleModel>) result.getResult();

        Assert.assertEquals(1, rules.size());

        RuleModel rule = rules.get(0);

        Assert.assertEquals("braces", rule.getName());

        SumExpressionModel sum = (SumExpressionModel) rule.getExpression();

        List<IExpressionModel> variants = sum.getExpressions();

        Assert.assertEquals(2, variants.size());

        ProductExpressionModel firstVariant = (ProductExpressionModel) variants.get(0);
        List<IExpressionModel> firstVariantExprs = firstVariant.getExpressions();

        LiteralExpressionModel secondVariant = (LiteralExpressionModel) variants.get(1);
        Assert.assertEquals("()", secondVariant.getLiteral());

        Assert.assertEquals(null, firstVariant.getCode());
        Assert.assertEquals(3, firstVariantExprs.size());

        Assert.assertEquals("(", ((LiteralExpressionModel) firstVariantExprs.get(0)).getLiteral());
        Assert.assertEquals(")", ((LiteralExpressionModel) firstVariantExprs.get(2)).getLiteral());

        NameExpressionModel name = (NameExpressionModel) firstVariantExprs.get(1);
        Assert.assertEquals("braces", name.getName());
    }
}
