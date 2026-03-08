/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.DSL
 *  com.mojang.datafixers.DataFix
 *  com.mojang.datafixers.OpticFinder
 *  com.mojang.datafixers.TypeRewriteRule
 *  com.mojang.datafixers.Typed
 *  com.mojang.datafixers.schemas.Schema
 *  com.mojang.datafixers.types.Type
 *  com.mojang.datafixers.types.templates.List$ListType
 */
package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class RemoveBlockEntityTagFix
extends DataFix {
    private final Set<String> blockEntityIdsToDrop;

    public RemoveBlockEntityTagFix(Schema outputSchema, Set<String> blockEntityIdsToDrop) {
        super(outputSchema, true);
        this.blockEntityIdsToDrop = blockEntityIdsToDrop;
    }

    public TypeRewriteRule makeRule() {
        Type itemStackType = this.getInputSchema().getType(References.ITEM_STACK);
        OpticFinder itemTagF = itemStackType.findField("tag");
        OpticFinder itemBlockEntityF = itemTagF.type().findField("BlockEntityTag");
        Type entityType = this.getInputSchema().getType(References.ENTITY);
        OpticFinder fallingBlockF = DSL.namedChoice((String)"minecraft:falling_block", (Type)this.getInputSchema().getChoiceType(References.ENTITY, "minecraft:falling_block"));
        OpticFinder fallingBlockEntityTagF = fallingBlockF.type().findField("TileEntityData");
        Type structureType = this.getInputSchema().getType(References.STRUCTURE);
        OpticFinder blocksF = structureType.findField("blocks");
        OpticFinder blockTypeF = DSL.typeFinder((Type)((List.ListType)blocksF.type()).getElement());
        OpticFinder blockNbtF = blockTypeF.type().findField("nbt");
        OpticFinder blockEntityIdF = DSL.fieldFinder((String)"id", NamespacedSchema.namespacedString());
        return TypeRewriteRule.seq((TypeRewriteRule)this.fixTypeEverywhereTyped("ItemRemoveBlockEntityTagFix", itemStackType, input -> input.updateTyped(itemTagF, tag -> this.removeBlockEntity((Typed<?>)tag, (OpticFinder<?>)itemBlockEntityF, (OpticFinder<String>)blockEntityIdF, "BlockEntityTag"))), (TypeRewriteRule[])new TypeRewriteRule[]{this.fixTypeEverywhereTyped("FallingBlockEntityRemoveBlockEntityTagFix", entityType, input -> input.updateTyped(fallingBlockF, tag -> this.removeBlockEntity((Typed<?>)tag, (OpticFinder<?>)fallingBlockEntityTagF, (OpticFinder<String>)blockEntityIdF, "TileEntityData"))), this.fixTypeEverywhereTyped("StructureRemoveBlockEntityTagFix", structureType, input -> input.updateTyped(blocksF, tag -> tag.updateTyped(blockTypeF, blockTag -> this.removeBlockEntity((Typed<?>)blockTag, (OpticFinder<?>)blockNbtF, (OpticFinder<String>)blockEntityIdF, "nbt")))), this.convertUnchecked("ItemRemoveBlockEntityTagFix - update block entity type", this.getInputSchema().getType(References.BLOCK_ENTITY), this.getOutputSchema().getType(References.BLOCK_ENTITY))});
    }

    private Typed<?> removeBlockEntity(Typed<?> tag, OpticFinder<?> blockEntityF, OpticFinder<String> blockEntityIdF, String blockEntityFieldName) {
        Optional maybeBlockEntity = tag.getOptionalTyped(blockEntityF);
        if (maybeBlockEntity.isEmpty()) {
            return tag;
        }
        String blockEntityId = ((Typed)maybeBlockEntity.get()).getOptional(blockEntityIdF).orElse("");
        if (!this.blockEntityIdsToDrop.contains(blockEntityId)) {
            return tag;
        }
        return Util.writeAndReadTypedOrThrow(tag, tag.getType(), tagData -> tagData.remove(blockEntityFieldName));
    }
}

