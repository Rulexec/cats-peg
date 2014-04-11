package by.muna.java.compiler;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public class StringJavaFileObject extends SimpleJavaFileObject {
    protected String code;

    public StringJavaFileObject(String path, String className, String code) {
        super(java.net.URI.create(
            "file:///" + path + "/" + className + ".java"),
            JavaFileObject.Kind.SOURCE
        );

        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncErrors) {
        return code;
    }
}
