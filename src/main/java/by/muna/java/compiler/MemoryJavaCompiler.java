package by.muna.java.compiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryJavaCompiler {
    public static interface IRegisterOutput {
        void register(String name, MemoryJavaFileObject fileObject);
    }

    private ByteArrayClassLoader classLoader = new ByteArrayClassLoader(this.getClass().getClassLoader());
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    private Map<String, MemoryJavaFileObject> outputs = new HashMap<>();

    private MemoryJavaFileManager fileManager = new MemoryJavaFileManager(
        this.compiler.getStandardFileManager(null, null, null),
        MemoryJavaCompiler.this.outputs::put
    );

    public MemoryJavaCompiler() {

    }

    public void compile(List<JavaFileObject> files) {
        this.compiler.getTask(null, this.fileManager, null, null, null, files).call();

        for (Entry<String, MemoryJavaFileObject> outputEntry : this.outputs.entrySet()) {
            byte[] bytes = outputEntry.getValue().getBytes();
            this.classLoader.define(outputEntry.getKey(), bytes, 0, bytes.length);
        }

        this.outputs.clear();
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        return this.classLoader.loadClass(name);
    }
}
