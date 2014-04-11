package by.muna.java.compiler;

public class ByteArrayClassLoader extends ClassLoader {
    public ByteArrayClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class define(String name, byte[] bytes, int offset, int length) {
        return super.defineClass(name, bytes, offset, length);
    }
}
