package sanandreasp.core.manpack.transformer;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeVersion;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformMiscStuff implements IClassTransformer, Opcodes {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
//		if(transformedName.equals("net.minecraft.client.gui.inventory.GuiContainerCreative") && ForgeVersion.getBuildVersion() < 707)
//			return transformGuiCCreative(bytes);
		
		return bytes;
	}
	
	private byte[] transformGuiCCreative(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);
		MethodNode mn = ASMHelper.findMethod("renderCreativeTab", "(Lnet/minecraft/creativetab/CreativeTabs;)V", cn);

		InsnList needle = new InsnList();
		needle.add(new VarInsnNode(ALOAD, 0));
		needle.add(new VarInsnNode(ILOAD, 7));
		needle.add(new VarInsnNode(ILOAD, 8));
		needle.add(new VarInsnNode(ILOAD, 5));
		needle.add(new VarInsnNode(ILOAD, 6));
		needle.add(new IntInsnNode(BIPUSH, 28));
		needle.add(new VarInsnNode(ILOAD, 9));
		needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/gui/inventory/GuiContainerCreative", "drawTexturedModalRect", "(IIIIII)V"));

		InsnList call = new InsnList();
		call.add(new LabelNode());
		call.add(new InsnNode(FCONST_1));
		call.add(new InsnNode(FCONST_1));
		call.add(new InsnNode(FCONST_1));
		call.add(new MethodInsnNode(INVOKESTATIC, "org/lwjgl/opengl/GL11", "glColor3f", "(FFF)V"));
		
	    List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(mn.instructions, needle);
	    if(ret.size() != 1)
	        throw new RuntimeException("Needle not found in Haystack!");
	    
	    mn.instructions.insertBefore(ret.get(0), call);
	    bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}

}
