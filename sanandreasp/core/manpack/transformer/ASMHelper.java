package sanandreasp.core.manpack.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMHelper {
    public static ClassNode createClassNode(byte[] bytes)
    {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);
        return cnode;
    }

    public static byte[] createBytes(ClassNode cnode, int i)
    {
        ClassWriter cw = new ClassWriter(i);
        cnode.accept(cw);
        return cw.toByteArray();
    }
    
    public static MethodNode findMethod(String name, String desc, ClassNode cnode)
    {
        for(MethodNode mnode : cnode.methods)
            if(name.equals(mnode.name) && desc.equals(mnode.desc))
                return mnode;
        return null;
    }
}
