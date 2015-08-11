/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import com.google.common.io.Files;
import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ASMObfuscationHelper
{
    private static final String CACHE_MCP_SRG_FILE = "mods/sapmanpack/mcp_srg_%s.srg";

    private static final ASMObfuscationHelper INSTANCE = new ASMObfuscationHelper();

    private final Map<String, NameMapping> srgMappings = new HashMap<>();
    private final Map<String, NameMapping> srgMappingsCached = new HashMap<>();

    public static ASMObfuscationHelper getInstance() {
        return INSTANCE;
    }

    private ASMObfuscationHelper() {
    }

    public MethodInsnNode getMethodInsnNode(int opcode, String mcpMapping, boolean intf) {
        NameMapping mapping = this.getMapping(mcpMapping);
        return new MethodInsnNode(opcode, mapping.owner, ASMHelper.isMCP ? mapping.mcpName : mapping.srgName, mapping.desc, intf);
    }

    public FieldInsnNode getFieldInsnNode(int opcode, String mcpMapping) {
        String[] mapNameDesc = mcpMapping.split(" ");
        NameMapping mapping = this.getMapping(mapNameDesc[0]);
        return new FieldInsnNode(opcode, mapping.owner, ASMHelper.isMCP ? mapping.mcpName : mapping.srgName, mapNameDesc[1]);
    }

    public MethodNode findMethod(ClassNode cn, String mcpMapping) {
        NameMapping mapping = this.getMapping(mcpMapping);
        return ASMHelper.findMethod(cn, ASMHelper.isMCP ? mapping.mcpName : mapping.srgName, mapping.desc);
    }

    private NameMapping getMapping(String mcpMapping) {
        if( this.srgMappingsCached.isEmpty() ) {
            this.readCachedMcpSrgFile();
        }

        if( !this.srgMappingsCached.containsKey(mcpMapping) ) {
            this.readMcpSrgFile();
            if( !this.srgMappings.containsKey(mcpMapping) ) {
                ModCntManPack.MOD_LOG.log(Level.FATAL, String.format("Cannot procede with transforming classes! Mapping \"%s\" was not found!", mcpMapping));
                throw new RuntimeException("Mapping not found!");
            }
            this.srgMappingsCached.put(mcpMapping, this.srgMappings.get(mcpMapping));
            updateCacheMcpSrgFile(this.srgMappings.get(mcpMapping));
        }

        return this.srgMappingsCached.get(mcpMapping);
    }

    private void readMcpSrgFile() {
        try( BufferedReader brd = new BufferedReader(new FileReader(new File(ManPackLoadingPlugin.source, "assets/sapmanpack/mcp-srg.srg"))) ) {
            readMcpSrgFile(brd);
        } catch( IOException e ) {
            ModCntManPack.MOD_LOG.log(Level.INFO, "Can't read mcp-srg.srg from jar, you're most likely in a dev environment! Trying classpath loading instead.");
            try( BufferedReader brd = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/assets/sapmanpack/mcp-srg.srg"))) ) {
                readMcpSrgFile(brd);
            } catch( IOException e2 ) {
                e2.printStackTrace();
            }
        }
    }

    private void readMcpSrgFile(BufferedReader brd) throws IOException {
        while( brd.ready() ) {
            NameMapping mapping = new NameMapping(brd.readLine());
            this.srgMappings.put(String.format("%s/%s %s", mapping.owner, mapping.mcpName, mapping.desc), mapping);
        }
    }

    private void readCachedMcpSrgFile() {
        File cachedSrg = new File(ManPackLoadingPlugin.mcLoc, String.format(CACHE_MCP_SRG_FILE, ManPackLoadingPlugin.MC_VERSION));
        try( BufferedReader brd = new BufferedReader(new FileReader(cachedSrg)) ) {
            while( brd.ready() ) {
                NameMapping mapping = new NameMapping(brd.readLine());
                this.srgMappingsCached.put(String.format("%s/%s %s", mapping.owner, mapping.mcpName, mapping.desc), mapping);
            }
        } catch( IOException e ) {
            ModCntManPack.MOD_LOG.log(Level.INFO, "Can't read mcp-srg.srg from cache, using full mappings now now.");
        }
    }

    private static void updateCacheMcpSrgFile(NameMapping mappingToAppend) {
        File cachedSrg = new File(ManPackLoadingPlugin.mcLoc, String.format(CACHE_MCP_SRG_FILE, ManPackLoadingPlugin.MC_VERSION));
        try {
            Files.createParentDirs(cachedSrg);
            try( BufferedWriter bwt = new BufferedWriter(new FileWriter(cachedSrg, true)) ) {
                bwt.write(mappingToAppend.toString());
                bwt.newLine();
            }
        } catch( IOException e ) {
            ModCntManPack.MOD_LOG.log(Level.WARN, "Can't write cache mcp-srg.srg!", e);
        }
    }

    private static class NameMapping
    {
        private static final Pattern MD_PATTERN = Pattern.compile("MD: (.*)/(.*?) (.*?) .*/(.*?) .*");
        private static final Pattern FD_CL_PATTERN = Pattern.compile("(FD|CL): (.*)/(.*?) .*/(.*)");

        public final Type type;
        public final String owner;
        public final String mcpName;
        public final String srgName;
        public final String desc;

        private NameMapping(Type type, String owner, String mcpName, String srgName, String desc) {
            this.type = type;
            this.owner = owner;
            this.mcpName = mcpName;
            this.srgName = srgName;
            this.desc = desc;
        }

        private NameMapping(String line) {
            Matcher matcher;
            if( (matcher = MD_PATTERN.matcher(line)).matches() ) {
                this.type = Type.METHOD;
                this.owner = matcher.group(1);
                this.mcpName = matcher.group(2);
                this.srgName = matcher.group(4);
                this.desc = matcher.group(3);
            } else if( (matcher = FD_CL_PATTERN.matcher(line)).matches() ) {
                this.type = matcher.group(1).equals("FD") ? Type.FIELD : Type.CLASS;
                this.owner = matcher.group(2);
                this.mcpName = matcher.group(3);
                this.srgName = matcher.group(4);
                this.desc = "";
            } else {
                throw new RuntimeException("Cannot read line!");
            }
        }

        @Override
        public String toString() {
            switch( this.type ) {
                case CLASS:
                    return String.format("CL: %1$s/%2$s %1$s/%3$s", this.owner, this.mcpName, this.srgName);
                case FIELD:
                    return String.format("FD: %1$s/%2$s %1$s/%3$s", this.owner, this.mcpName, this.srgName);
                case METHOD:
                    return String.format("MD: %1$s/%2$s %4$s %1$s/%3$s %4$s", this.owner, this.mcpName, this.srgName, this.desc);
                default:
                    throw new RuntimeException("Type is not defined.");
            }
        }

        enum Type
        {
            CLASS, FIELD, METHOD
        }
    }
}
