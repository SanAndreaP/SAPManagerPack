/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.regex.Matcher;

public class AnnotationChecker
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        ClassNode cn = ASMHelper.createClassNode(bytes);
        for( MethodNode method : cn.methods ) {
            if( method.visibleAnnotations != null ) {
                for( AnnotationNode annotation : method.visibleAnnotations ) {
                    if( annotation.desc.equals("Lde/sanandrew/core/manpack/util/annotation/ASMOverride;") ) {
                        String asmMethodName = annotation.values.get(1).toString();
                        MethodNode asmMethod = getSignature(asmMethodName);
                        if( !method.name.equals(asmMethod.name) ) {
                            String err = "Attempting to override Method %s in Class %s with incompatible name %s!";
                            ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, String.format(err, asmMethod.name, cn.name, method.name));
                            throw new OverrideException(String.format("Method name %s is not equal to %s!", method.name, asmMethod.name));
                        } else if( !method.desc.equals(asmMethod.desc) ) {
                            String err = "Attempting to override Method %s, description %s, in Class %s with incompatible description %s!";
                            ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, String.format(err, asmMethod.name, asmMethod.desc, cn.name, method.desc));
                            throw new OverrideException(String.format("Method desc %s is not equal to %s!", method.desc, asmMethod.desc));
                        } else if( getAccessLevelInt(method.access) < getAccessLevelInt(asmMethod.access) ) {
                            String err = "Attempting to assign weaker access privileges ('%s') on %s in %s; should be '%s'";
                            ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, String.format(err, getAccessLevelName(method.access), asmMethod.name, cn.name,
                                                                                        getAccessLevelName(asmMethod.access)));
                            throw new OverrideException(String.format("Access level %s is weaker than %s!", getAccessLevelName(method.access),
                                                                      getAccessLevelName(asmMethod.access)));
                        } else if( getAccessLevelInt(asmMethod.access) == 1 ) {
                            String classPkg = cn.name.substring(0, cn.name.lastIndexOf('/'));
                            String ownerPkg = ASMHelper.getMethodInsnNode(asmMethod.access, asmMethodName, false).owner.substring(0, cn.name.lastIndexOf('/'));
                            if( !classPkg.equals(ownerPkg) ) {
                                String err = "Attempting to override packageLocal method %s outside of package %s in class %s, which is in package %s!";
                                ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, String.format(err, method.name, ownerPkg, cn.name, classPkg));
                                throw new OverrideException(String.format("Class %s lies outside package %s for method %s to be overridden", cn.name, ownerPkg,
                                                                          classPkg));
                            }
                        } else if( checkBitwiseEqual(asmMethod.access, Opcodes.ACC_DEPRECATED) ) {
                            ManPackLoadingPlugin.MOD_LOG.log(Level.WARN, String.format("The Method %s is marked as deprecated! It is most likely that the method is " +
                                                                                               "not injected in any superclass anymore! Thus this may not be called!",
                                                                                       asmMethod.name));
                        }
                    }
                }
            }
        }

        return bytes;
    }

    private static MethodNode getSignature(String method) {
        String split[] = method.split(" ");

        int accLvl = getAccessLevelOpcode(split[0]);
        Matcher mtch = ASMNames.OWNERNAME.matcher(split[1]);
        if( !mtch.find() ) {
            throw new RuntimeException("SAP-Method signature invalid!");
        }

        String name = mtch.group(2);
        String desc = split[2];
        String sig = split.length > 3 ? split[3] : null;
        String throwing[] = split.length > 4 ? split[4].split(";") : null;

        return new MethodNode(accLvl, name, desc, sig, throwing);
    }

    private static int getAccessLevelInt(int access) {
        if( checkBitwiseEqual(access, Opcodes.ACC_PRIVATE) ) {
            return 0;
        } else if( checkBitwiseEqual(access, Opcodes.ACC_PROTECTED) ) {
            return 2;
        } else if( checkBitwiseEqual(access, Opcodes.ACC_PUBLIC) ) {
            return 3;
        } else {    // if ACC_PACKAGE_LOCAL
            return 1;
        }
    }

    private static String getAccessLevelName(int access) {
        if( checkBitwiseEqual(access, Opcodes.ACC_PRIVATE) ) {
            return "private";
        } else if( checkBitwiseEqual(access, Opcodes.ACC_PROTECTED) ) {
            return "protected";
        } else if( checkBitwiseEqual(access, Opcodes.ACC_PUBLIC) ) {
            return "public";
        } else {    // if ACC_PACKAGE_LOCAL
            return "packageLocal";
        }
    }

    private static int getAccessLevelOpcode(String accessName) {
        String[] accessSplit = accessName.split(";");
        int ret = 0;
        for( String acc : accessSplit ) {
            switch( acc ) {
                case "private":
                    ret |= Opcodes.ACC_PRIVATE;
                    break;
                case "protected":
                    ret |= Opcodes.ACC_PROTECTED;
                    break;
                case "public":
                    ret |= Opcodes.ACC_PUBLIC;
                    break;
                case "deprecated":
                    ret |= Opcodes.ACC_DEPRECATED;
                    break;
            }
        }

        return ret;
    }

    private static boolean checkBitwiseEqual(int value, int flag) {
        return (value & flag) == flag;
    }

    public static class OverrideException
            extends RuntimeException
    {
        private static final long serialVersionUID = 7395488467026629355L;

        public OverrideException(String message) {
            super(message);
        }
    }
}
