package by.muna.java.compiler;

import by.muna.java.compiler.MemoryJavaCompiler.IRegisterOutput;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class MemoryJavaFileManager implements JavaFileManager {
    protected JavaFileManager parent;
    private IRegisterOutput register;

    public MemoryJavaFileManager(JavaFileManager parent, IRegisterOutput register) {
        this.parent = parent;
        this.register = register;
    }

    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        String filePath = className.replace('.', '/');

        MemoryJavaFileObject fileObject = new MemoryJavaFileObject("file:///" + filePath + ".class", kind);

        this.register.register(className, fileObject);

        return fileObject;
    }

    public void close() throws IOException { parent.close(); }
    public void flush() throws IOException { parent.flush(); }
    public ClassLoader getClassLoader(JavaFileManager.Location location) { return parent.getClassLoader(location); }
    public FileObject getFileForInput(JavaFileManager.Location location, String packageName, String relName) throws IOException { return parent.getFileForInput(location, packageName, relName); }
    public FileObject getFileForOutput(JavaFileManager.Location location, String packageName, String relName, FileObject sibling) throws IOException { return parent.getFileForOutput(location, packageName, relName, sibling); }
    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException { return parent.getJavaFileForInput(location, className, kind); }
    public boolean handleOption(String current, Iterator<String> remaining) { return parent.handleOption(current, remaining); }
    public boolean hasLocation(JavaFileManager.Location location) { return parent.hasLocation(location); }
    public String inferBinaryName(JavaFileManager.Location location, JavaFileObject file) { return parent.inferBinaryName(location, file); }
    public boolean isSameFile(FileObject a, FileObject b) { return parent.isSameFile(a, b); }
    public Iterable<JavaFileObject> list(JavaFileManager.Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException { return parent.list(location, packageName, kinds, recurse); }
    public int isSupportedOption(String option) { return parent.isSupportedOption(option); }
}
