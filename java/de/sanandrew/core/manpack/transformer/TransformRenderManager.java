/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformRenderManager
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if( "net.minecraft.client.renderer.entity.RenderManager".equals(transformedName) ) {
            ClassNode cn = ASMHelper.createClassNode(basicClass);
            transformGetERenderObj(ASMHelper.findMethod(ASMNames.M_getEntityRenderObj, "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;", cn));

            basicClass = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            return basicClass;
        }

        return basicClass;
    }

    private static void transformGetERenderObj(MethodNode method) {
        InsnList needle = new InsnList();

        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;", false));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RenderManager", ASMNames.M_getEntityClsRenderObj, "(Ljava/lang/Class;)Lnet/minecraft/client/renderer/entity/Render;", false));
        needle.add(new InsnNode(Opcodes.ARETURN));

        AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();

        newInstr.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/client/event/GetRenderObjEvent"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/client/event/GetRenderObjEvent", "<init>", "(Lnet/minecraft/entity/Entity;)V", false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 2));
        newInstr.add(new LabelNode());
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false));
        newInstr.add(new InsnNode(Opcodes.POP));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "de/sanandrew/core/manpack/util/client/event/GetRenderObjEvent", "newReturn", "Lnet/minecraft/client/renderer/entity/Render;"));
        LabelNode l3 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFNULL, l3));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "de/sanandrew/core/manpack/util/client/event/GetRenderObjEvent", "newReturn", "Lnet/minecraft/client/renderer/entity/Render;"));
        newInstr.add(new InsnNode(Opcodes.ARETURN));
        newInstr.add(l3);
        newInstr.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[]{"de/sanandrew/core/manpack/util/client/event/GetRenderObjEvent"}, 0, null));

        method.instructions.insertBefore(insertPt, newInstr);
    }

    //  how it looks

//    public Render getEntityRenderObject(Entity p_78713_1_)
//    {
//        GetRenderObjEvent evt = new GetRenderObjEvent(p_78713_1_); MinecraftForge.EVENT_BUS.post(evt); if(evt.newReturn!=null)return evt.newReturn;
//        return RenderManager.instance.getEntityClassRenderObject(p_78713_1_.getClass());
//    }
}
