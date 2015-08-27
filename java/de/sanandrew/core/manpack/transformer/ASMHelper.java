package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class ASMHelper
{
    /** A boolean which is true when we are in a development environment **/
    public static boolean isMCP = false;

    /**
     * Creates a byte-array representation of the supplied ClassNode.
     *
     * @param cnode     the ClassNode to be converted into a byte array
     * @param cwFlags   the flags to be supplied to the ClassWriter. You almost always want both COMPUTE_FRAMES and COMPUTE_MAXS
     * @return a byte array representation of the ClassNode to be written back to the ClassLoader
     */
    public static byte[] createBytes(ClassNode cnode, int cwFlags) {
        ClassWriter cw = new ClassWriter(cwFlags);
        cnode.accept(cw);
        byte[] bArr = cw.toByteArray();
        ManPackLoadingPlugin.MOD_LOG.log(Level.INFO, String.format("Class %s successfully transformed!", cnode.name));
        return bArr;
    }

    /**
     * Creates a ClassNode from a byte array to be used for ASM modifications.
     *
     * @param bytes     A byte array representing the class to be modified by ASM
     * @return a new ClassNode instance
     */
    public static ClassNode createClassNode(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);
        return cnode;
    }

    /**
     * Searches for the instruction set (needle) inside an another instruction set (haystack) and returns the first instruction node from the found needle.
     *
     * @param haystack  The instruction set to be searched in
     * @param needle    The instruction set to search for
     * @return The first instruction node from the haystack on the found position
     * @throws de.sanandrew.core.manpack.transformer.ASMHelper.InvalidNeedleException when the needle was not found or was found multiple times
     */
    public static AbstractInsnNode findFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);

        if( ret.size() != 1 ) {
            throw new InvalidNeedleException(ret.size());
        }

        return ret.get(0);
    }

    /**
     * Searches for the instruction set (needle) inside an another instruction set (haystack) and returns the last instruction node from the found needle.
     *
     * @param haystack  The instruction set to be searched in
     * @param needle    The instruction set to search for
     * @return The last instruction node from the haystack on the found position
     * @throws de.sanandrew.core.manpack.transformer.ASMHelper.InvalidNeedleException when the needle was not found or was found multiple times
     */
    public static AbstractInsnNode findLastNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(haystack, needle);

        if( ret.size() != 1 ) {
            throw new InvalidNeedleException(ret.size());
        }

        return ret.get(0);
    }

    /**
     * Scans the ClassNode for a method name
     *
     * @param cn        the ClassNode to be searched in
     * @param method    The method name to search for
     * @return true, if the name was found, or else false
     */
    public static boolean hasClassMethod(ClassNode cn, String method) {
        Triplet<String, String, String[]> methodDesc = ASMNames.getSrgNameMd(method);

        for( MethodNode methodNd : cn.methods ) {
            if( methodNd.name.equals(methodDesc.getValue1()) && methodNd.desc.equals(methodDesc.getValue2()[0]) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Scans the ClassNode for a method name and descriptor
     *
     * @param cnode     the ClassNode to be searched in
     * @param name      The method name to search for
     * @param desc      The method descriptor to search for
     * @return true, if the name was found, or else false
     * @throws de.sanandrew.core.manpack.transformer.ASMHelper.MethodNotFoundException when the method name and descriptor couldn't be found
     */
    private static MethodNode findMethodNode(ClassNode cnode, String name, String desc) {
        for( MethodNode mnode : cnode.methods ) {
            if( name.equals(mnode.name) && desc.equals(mnode.desc) ) {
                return mnode;
            }
        }
        throw new MethodNotFoundException(name, desc);
    }

    /**
     * Gets the appropriate field/method name for either it is called in a development or productive environment
     *
     * @param mcp The name to be used in a development environment
     * @param srg The name to be used in a productive environment
     * @return The right name appropriate to the environment
     */
    public static String getRemappedMF(String mcp, String srg) {
        if( ASMHelper.isMCP ) {
            return mcp;
        }
        return srg;
    }

    /**
     * removes an entire instruction set in the haystack.
     *
     * @param haystack  The instruction set to be searched in
     * @param needle    The instruction set to search for and to be removed
     */
    public static void removeNeedleFromHaystack(InsnList haystack, InsnList needle) {
        int firstInd = haystack.indexOf(findFirstNodeFromNeedle(haystack, needle));
        int lastInd = haystack.indexOf(findLastNodeFromNeedle(haystack, needle));
        List<AbstractInsnNode> realNeedle = new ArrayList<>();

        for( int i = firstInd; i <= lastInd; i++ ) {
            realNeedle.add(haystack.get(i));
        }

        for( AbstractInsnNode node : realNeedle ) {
            haystack.remove(node);
        }
    }

    /**
     * Writes the class bytes into a file. Helpful for debugging ASM transformations.<br>
     * Note: this will write the bytes as compiled .class file! Use JD-GUI to look into the class.
     *
     * @param classBytes    The class bytes to be written as a class file
     * @param file          The filename (inclusive path) the bytes will be saved in
     */
    public static void writeClassToFile(byte[] classBytes, String file) {
        try( FileOutputStream out = new FileOutputStream(file) ) {
            out.write(classBytes);
        } catch( Throwable e ) {
            e.printStackTrace();
        }
    }

    public static MethodNode getMethodNode(int access, String method) {
        Triplet<String, String, String[]> methodDesc = ASMNames.getSrgNameMd(method);
        String sig = methodDesc.getValue2().length > 1 ? methodDesc.getValue2()[1] : null;
        String throwing[] = methodDesc.getValue2().length > 2 ? methodDesc.getValue2()[2].split(";") : null;

        return new MethodNode(access, methodDesc.getValue1(), methodDesc.getValue2()[0], sig, throwing);
    }

    public static MethodInsnNode getMethodInsnNode(int opcode, String method, boolean intf) {
        Triplet<String, String, String[]> methodDesc = ASMNames.getSrgNameMd(method);

        return new MethodInsnNode(opcode, methodDesc.getValue0(), methodDesc.getValue1(), methodDesc.getValue2()[0], intf);
    }

    public static void visitMethodInsn(MethodNode node, int opcode, String method, boolean intf) {
        MethodInsnNode mdiNode = getMethodInsnNode(opcode, method, intf);
        node.visitMethodInsn(opcode, mdiNode.owner, mdiNode.name, mdiNode.desc, intf);
    }

    public static MethodNode findMethod(ClassNode clazz, String method) {
        Triplet<String, String, String[]> methodDesc = ASMNames.getSrgNameMd(method);

        return findMethodNode(clazz, methodDesc.getValue1(), methodDesc.getValue2()[0]);
    }

    public static FieldInsnNode getFieldInsnNode(int opcode, String field) {
        Triplet<String, String, String> fieldDesc = ASMNames.getSrgNameFd(field);

        return new FieldInsnNode(opcode, fieldDesc.getValue0(), fieldDesc.getValue1(), fieldDesc.getValue2());
    }

    public static void visitFieldInsn(MethodNode node, int opcode, String field) {
        FieldInsnNode fdiNode = getFieldInsnNode(opcode, field);
        node.visitFieldInsn(opcode, fdiNode.owner, fdiNode.name, fdiNode.desc);
    }

    public static class InvalidNeedleException
            extends RuntimeException
    {
        private static final long serialVersionUID = -913530798954926801L;

        public InvalidNeedleException(int count) {
            super(count > 1 ? "Multiple Needles found in Haystack!" : count < 1 ? "Needle not found in Haystack!" : "Wait, Needle was found!? o.O");
        }
    }

    public static class MethodNotFoundException
            extends RuntimeException
    {
        private static final long serialVersionUID = 7439846361566319105L;

        public MethodNotFoundException(String methodName, String methodDesc) {
            super(String.format("Could not find any method matching the name < %s > and description < %s >", methodName, methodDesc));
        }
    }
}
