package by.muna.java.compiler.tests;

import by.muna.java.compiler.MemoryJavaCompiler;
import by.muna.java.compiler.StringJavaFileObject;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MemoryJavaCompilerTest {
    @Test
    public void helloWorldTest() throws ReflectiveOperationException {
        String code =
            "package by.muna.temp.codegen; public class HelloWorld {" +
            "public static int getAnswer(int seven) {" +
            "return seven == 7 ? 42 : 13;" +
            "}" +
            "}";

        MemoryJavaCompiler compiler = new MemoryJavaCompiler();

        compiler.compile(Arrays.asList(new StringJavaFileObject(
            "by/muna/temp/codegen", "HelloWorld", code
        )));

        Class helloWorldClass = compiler.loadClass("by.muna.temp.codegen.HelloWorld");

        Method method = helloWorldClass.getMethod("getAnswer", int.class);
        int result = (Integer) method.invoke(null, 5);

        Assert.assertEquals(13, result);

        result = (Integer) method.invoke(null, 7);

        Assert.assertEquals(42, result);
    }
}
