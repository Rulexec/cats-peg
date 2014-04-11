package by.muna.java.compiler;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class MemoryJavaFileObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    public MemoryJavaFileObject(String uri, JavaFileObject.Kind kind) {
        super(java.net.URI.create(uri), kind);
    }

    public byte[] getBytes() {
        return this.out.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
        return out;
    }
}
