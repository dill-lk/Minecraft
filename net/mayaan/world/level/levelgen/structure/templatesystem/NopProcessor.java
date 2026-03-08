/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.MapCodec;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class NopProcessor
extends StructureProcessor {
    public static final MapCodec<NopProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final NopProcessor INSTANCE = new NopProcessor();

    private NopProcessor() {
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.NOP;
    }
}

