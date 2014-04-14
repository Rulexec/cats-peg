Cats PEG parser (java)
=====================

Work is in progress. Many optimisations are needed, generator isn't implemented yet, there's no ability to init a parser or define functions (but you can import some java classes and use static methods).

Syntax syntax
---------

In ```<>``` braces — literated content.

The syntax is a list of rules, optionally prepended with a list of directives:

```
#directiveName 'directiveStringValue'
```
```
ruleName = <expression>
```

Expression can be:

- ```.``` (dot) — any char
- ```[0-9ab]``` — char ```a```, or ```b```, or from ```0``` to ```9```
- ```'literal'``` — match string ```literal```
- ```<expression> <expression>``` — __AND__, for example, ```. [0-9]```, accepts any char with following digit, for example, ```x7```
- ```<expression> / <expression>``` — OR, for example, ```[0-9] / [a-z] .```, accepts a digit or a char in the a-z interval followed by any char, for example, ```q*```
- ```(<expression>)``` — grouped expression
- ```& <expression>``` — lookahead expression. Doesn't change parse position and continues parsing if mathces, otherwise fails. Example: ```. & 'og'``` matches ```dog```, but parsing continues from ```og```
- ```! <expression>``` — lookahead expression similar to ```&```, but with negation, so ```. & 'og'``` not matches ```dog```, but matches ```cat``` "eating" only letter ```c```
- ```<expression>Q``` where Q is ```?``` or ```+``` or ```*``` or ```<<number>, <number>>``` or ```<<number>, *>```. ```?``` matches none of one, ```+``` one or more, ```*``` zero or more, ```<A, B>``` matches from ```A``` to ```B```, or to infinity if ```B``` is an asterisk
- ```<expression> {@ java code @}``` — transforms the result of parsing. Also, an expressions must be named (```a:<expression>```), and optionally typed (```(a #type 'List<Character>'):[0-9]+```, otherwise in the code you'll get an Object, which you have to cast yourself). Lookahead and predicates cannot be named (actually they can, if grouped, but it's useless)
- ```& {@ java code @}``` — predicate, have access to named variables and must return boolean. Parsing fails if a predicate have returned false
- ```! {@ java code @}``` — predicate, negation of ```&```, if true, then parsing is fails

Example:

```
#javaImport 'java.util.List'

start = WS? a:additive WS? {@ return a; @}

additive = (a #type 'Integer'):multiplicative WS? '+' WS? (b #type 'Integer'):additive {@
   return a + b;
@} / multiplicative

multiplicative = (a #type 'Integer'):primary WS? '*' WS? (b #type 'Integer'):multiplicative {@
   return a * b;
@} / primary

primary = integer / '(' WS? a:additive WS? ')' {@
   return a;
@}

integer = (chars #type 'List<Character>'):[0-9]+ {@
   StringBuilder sb = new StringBuilder();
   for (char c : chars) sb.append(c);
   return Integer.parseUnsignedInt(sb.toString());
@}

WS = [ \t\r\n]+
```

Dev parser
---------

You can build this project with ```gradle jar``` and get the executable jar-file from ```build/libs/```.

It receives three parameters:

- path to syntax file
- path to text file
- _(optional)_ start rule (```start``` by default)

But if you want to import non-standard classes, you have to run it this way: ```java -cp /home/ruliov/gradleJarsRepo/cats-peg-0.2.0.jar:<classpath> by.muna.peg.Main <args>```.

Java API
---------

### Parser

#### PEGParser&lt;T&gt; from _by.muna.peg_

```
T parse(String text) throws PEGParseException
```

### Parsing syntax

#### SelfParser extends PEGParser&lt;SyntaxModel&gt; from _by.muna.peg.self_

```
SelfParser()
```

Constructs ```PEGParser<SyntaxModel>``` to parse some syntax.

### Parsing text

#### PEGInterpretativeParser&lt;T&gt; extends PEGParser&lt;T&gt; from _by.muna.peg.interpreter_

```
PEGInterpretativeParser(SyntaxModel syntaxModel, String rule)
```

Gets syntaxModel (for example from ```SelfParser```), startRule and creates ```PEGParser```.

Example:

```
import by.muna.peg.interpreter.PEGInterpretativeParser;
import by.muna.peg.self.SelfParser;

public class Main {
    public static void main(String[] args) throws Exception {
        int parsed = new PEGInterpretativeParser<Integer>(new SelfParser().parse(
            "#javaImport 'java.util.List'\n" +
            "start = (chars #type 'List<Character>'):[0-9]+ {@" +
                "StringBuilder sb = new StringBuilder();" +
                "for (char c : chars) sb.append(c);" +
                "return Integer.parseInt(sb.toString());" +
            "@}"
        ), "start").parse("42");

        System.out.println(parsed); // 42
    }
}
```

### Tools

#### DevelopmentParsing from _by.muna.peg.tools_

```
static String parse(String syntax, String text) throws PEGParseException
static String parse(String syntax, String text, String startRule) throws PEGParseException
```

Accepts syntax and text and returns ```OutputFormatter.format(o)```.

#### OutputFormatter from _by.muna.peg.tools_

```
static String format(Object o)
```

Custom ```toString```.