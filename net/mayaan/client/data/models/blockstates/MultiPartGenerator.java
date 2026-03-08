/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.data.models.blockstates;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.mayaan.client.data.models.MultiVariant;
import net.mayaan.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.mayaan.client.data.models.blockstates.ConditionBuilder;
import net.mayaan.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.mayaan.client.renderer.block.dispatch.multipart.Condition;
import net.mayaan.client.renderer.block.dispatch.multipart.Selector;
import net.mayaan.world.level.block.Block;

public class MultiPartGenerator
implements BlockModelDefinitionGenerator {
    private final Block block;
    private final List<Entry> parts = new ArrayList<Entry>();

    private MultiPartGenerator(Block block) {
        this.block = block;
    }

    @Override
    public Block block() {
        return this.block;
    }

    public static MultiPartGenerator multiPart(Block block) {
        return new MultiPartGenerator(block);
    }

    public MultiPartGenerator with(MultiVariant variants) {
        this.parts.add(new Entry(Optional.empty(), variants));
        return this;
    }

    private void validateCondition(Condition condition) {
        condition.instantiate(this.block.getStateDefinition());
    }

    public MultiPartGenerator with(Condition condition, MultiVariant variants) {
        this.validateCondition(condition);
        this.parts.add(new Entry(Optional.of(condition), variants));
        return this;
    }

    public MultiPartGenerator with(ConditionBuilder condition, MultiVariant variants) {
        return this.with(condition.build(), variants);
    }

    @Override
    public BlockStateModelDispatcher create() {
        return new BlockStateModelDispatcher(Optional.empty(), Optional.of(new BlockStateModelDispatcher.MultiPartDefinition(this.parts.stream().map(Entry::toUnbaked).toList())));
    }

    private record Entry(Optional<Condition> condition, MultiVariant variants) {
        public Selector toUnbaked() {
            return new Selector(this.condition, this.variants.toUnbaked());
        }
    }
}

