package sanandreasp.core.manpack.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.common.asm.transformers.deobf.LZMAInputSupplier;

public class ASMHelper
{
	private static Map<String, Map<String, String>> methods = Maps.newHashMap();
	private static Map<String, Map<String, String>> fields = Maps.newHashMap();
	public static boolean isMCP = false;
	
	public static void setup(File mcDir, LaunchClassLoader classLoader, String deobfFileName) {
        try {
            InputStream classData = ASMHelper.class.getResourceAsStream(deobfFileName);
            LZMAInputSupplier zis = new LZMAInputSupplier(classData);
            InputSupplier<InputStreamReader> srgSupplier = CharStreams.newReaderSupplier(zis,Charsets.UTF_8);
            List<String> srgList = CharStreams.readLines(srgSupplier);
            Splitter splitter = Splitter.on(CharMatcher.anyOf(": ")).omitEmptyStrings().trimResults();
            
            for( String line : srgList ) {
                String[] parts = Iterables.toArray(splitter.split(line),String.class);
                String typ = parts[0];
                
                if( "MD".equals(typ) ) {
                	ASMHelper.parseMethod(parts);
                } else if( "FD".equals(typ) ) {
                	ASMHelper.parseField(parts);
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeClassToFile(byte[] classBytes, String file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(classBytes);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static void parseMethod(String[] parts) {
        String oldSrg = parts[1];
        int lastOld = oldSrg.lastIndexOf('/');
        String cl = oldSrg.substring(0,lastOld);
        String oldName = oldSrg.substring(lastOld+1);
        String newSrg = parts[3];
        int lastNew = newSrg.lastIndexOf('/');
        String newName = newSrg.substring(lastNew+1);
        
        if( !ASMHelper.methods.containsKey(cl) ) {
        	ASMHelper.methods.put(cl, Maps.<String,String>newHashMap());
        }
        ASMHelper.methods.get(cl).put(newName, oldName);
    }

    private static void parseField(String[] parts) {
        String oldSrg = parts[1];
        int lastOld = oldSrg.lastIndexOf('/');
        String cl = oldSrg.substring(0,lastOld);
        String oldName = oldSrg.substring(lastOld+1);
        String newSrg = parts[2];
        int lastNew = newSrg.lastIndexOf('/');
        String newName = newSrg.substring(lastNew+1);
        
        if( !ASMHelper.fields.containsKey(cl) ) {
        	ASMHelper.fields.put(cl, Maps.<String,String>newHashMap());
        }
        ASMHelper.fields.get(cl).put(newName, oldName);
    }
	
    public static ClassNode createClassNode(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);
        return cnode;
    }

    public static byte[] createBytes(ClassNode cnode, int i) {
        ClassWriter cw = new ClassWriter(i);
        cnode.accept(cw);
        return cw.toByteArray();
    }
    
    public static MethodNode findMethod(String name, String desc, ClassNode cnode) {
        for( MethodNode mnode : (List<MethodNode>)cnode.methods ) {
            if( name.equals(mnode.name) && desc.equals(mnode.desc) ) {
                return mnode;
            }
        }
        return null;
    }
    
    public static String getNotchedMethod(String MCP, String SRG) {
    	if( ASMHelper.isMCP ) {
    		return MCP;
    	}
    	
    	String clazz = getNotchedClassName(SRG.substring(0, SRG.lastIndexOf('/')));
    	SRG = SRG.substring(SRG.lastIndexOf('/')+1);
    	
    	if( ASMHelper.methods.containsKey(clazz) && ASMHelper.methods.get(clazz).containsKey(SRG) ) {
			return ASMHelper.methods.get(clazz).get(SRG);
    	} else {
    		return SRG;
    	}
    }
    
    public static String getNotchedField(String MCP, String SRG) {
    	if( ASMHelper.isMCP ) {
    		return MCP;
    	}
    	
    	String clazz = getNotchedClassName(SRG.substring(0, SRG.lastIndexOf('/')));
    	SRG = SRG.substring(SRG.lastIndexOf('/')+1);
    	
    	if( ASMHelper.fields.containsKey(clazz) && ASMHelper.fields.get(clazz).containsKey(SRG) ) {
			return ASMHelper.fields.get(clazz).get(SRG);
    	} else {
    		return SRG;
    	}
    }
    
    public static String getRemappedMF(String MCP, String SRG) {
    	if( ASMHelper.isMCP ) {
    		return MCP;
    	}
    	return SRG;
    }
    
    public static String getNotchedClassName(String SRG) {
        if( ASMHelper.isMCP ) {
            return SRG;
        }
    	return FMLDeobfuscatingRemapper.INSTANCE.unmap(SRG);
    }
    
    public static AbstractInsnNode findLastNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(haystack, needle);
        
        if( ret.size() < 1 ) {
            throw new RuntimeException("Needle not found in Haystack!");
        }
        if( ret.size() > 1 ) {
            throw new RuntimeException("Multiple Needles found in Haystack!");
        }
        
        return ret.get(0);
    }
    
    public static void remLastNodeFromNeedle(InsnList haystack, InsnList needle) {
        haystack.remove(ASMHelper.findLastNodeFromNeedle(haystack, needle));
    }
    
    public static AbstractInsnNode findFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);
        
        if( ret.size() < 1 ) {
            throw new RuntimeException("Needle not found in Haystack!");
        }
        if( ret.size() > 1 ) {
            throw new RuntimeException("Multiple Needles found in Haystack!");
        }
        
        return ret.get(0);
    }
    
    public static void remFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        haystack.remove(ASMHelper.findFirstNodeFromNeedle(haystack, needle));
    }
}
