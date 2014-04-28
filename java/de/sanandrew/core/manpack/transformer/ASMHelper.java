package de.sanandrew.core.manpack.transformer;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.FMLLog;

public class ASMHelper
{
    public static boolean isMCP = false;

    public static byte[] createBytes(ClassNode cnode, int i) {
        ClassWriter cw = new ClassWriter(i);
        cnode.accept(cw);
        byte[] bArr = cw.toByteArray();
        FMLLog.log("SAPManPack", Level.INFO, "Class %s successfully transformed!", cnode.name);
        return bArr;
    }

    public static ClassNode createClassNode(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);
        return cnode;
    }

    public static AbstractInsnNode findFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);

        if( ret.size() != 1 ) {
            throw new InvalidNeedleException(ret.size());
        }

        return ret.get(0);
    }

    public static boolean hasClassMethodName(ClassNode cn, String methodName) {
        for( MethodNode method : cn.methods ) {
            if( method.name.equals(methodName) ) {
                return true;
            }
        }

        return false;
    }

    public static AbstractInsnNode findLastNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(haystack, needle);

        if( ret.size() != 1 ) {
            throw new InvalidNeedleException(ret.size());
        }

        return ret.get(0);
    }

    public static MethodNode findMethod(String name, String desc, ClassNode cnode) {
        for( MethodNode mnode : cnode.methods ) {
            if( name.equals(mnode.name) && desc.equals(mnode.desc) ) {
                return mnode;
            }
        }
        throw new MethodNotFoundException(name, desc);
    }

    public static String getRemappedMF(String MCP, String SRG) {
        if( ASMHelper.isMCP ) {
            return MCP;
        }
        return SRG;
    }

    public static void remFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        haystack.remove(ASMHelper.findFirstNodeFromNeedle(haystack, needle));
    }

    public static void remLastNodeFromNeedle(InsnList haystack, InsnList needle) {
        haystack.remove(ASMHelper.findLastNodeFromNeedle(haystack, needle));
    }

    public static void removeNeedleFromHaystack(InsnList haystack, InsnList needle) {
        int firstInd = haystack.indexOf(findFirstNodeFromNeedle(haystack, needle));
        int lastInd = haystack.indexOf(findLastNodeFromNeedle(haystack, needle));
        List<AbstractInsnNode> realNeedle = new ArrayList<AbstractInsnNode>();

        for( int i = firstInd; i <= lastInd; i++ ) {
            realNeedle.add(haystack.get(i));
        }

        for( AbstractInsnNode node : realNeedle ) {
            haystack.remove(node);
        }
    }

    public static void writeClassToFile(byte[] classBytes, String file) {
        try( FileOutputStream out = new FileOutputStream(file) ) {
            out.write(classBytes);
        } catch( Throwable e ) {
            e.printStackTrace();
        }
    }

    public static class InvalidNeedleException
        extends RuntimeException
    {
        private static final long serialVersionUID = -913530798954926801L;

        public InvalidNeedleException(int count) {
            super(count > 1 ? "Multiple Needles found in Haystack!" : "Needle not found in Haystack!");
        }
    }

    public static class MethodNotFoundException
        extends RuntimeException
    {
        private static final long serialVersionUID = 7439846361566319105L;

        public MethodNotFoundException(String methodName, String methodDesc) {
            super(String.format("Could not find any method matching the name < %s > and description < %s >",
                                methodName, methodDesc));
        }
    }
}
