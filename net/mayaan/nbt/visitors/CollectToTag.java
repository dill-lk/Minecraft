/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.nbt.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import net.mayaan.nbt.ByteArrayTag;
import net.mayaan.nbt.ByteTag;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.nbt.DoubleTag;
import net.mayaan.nbt.EndTag;
import net.mayaan.nbt.FloatTag;
import net.mayaan.nbt.IntArrayTag;
import net.mayaan.nbt.IntTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.LongArrayTag;
import net.mayaan.nbt.LongTag;
import net.mayaan.nbt.ShortTag;
import net.mayaan.nbt.StreamTagVisitor;
import net.mayaan.nbt.StringTag;
import net.mayaan.nbt.Tag;
import net.mayaan.nbt.TagType;
import org.jspecify.annotations.Nullable;

public class CollectToTag
implements StreamTagVisitor {
    private final Deque<ContainerBuilder> containerStack = new ArrayDeque<ContainerBuilder>();

    public CollectToTag() {
        this.containerStack.addLast(new RootBuilder());
    }

    public @Nullable Tag getResult() {
        return this.containerStack.getFirst().build();
    }

    protected int depth() {
        return this.containerStack.size() - 1;
    }

    private void appendEntry(Tag instance) {
        this.containerStack.getLast().acceptValue(instance);
    }

    @Override
    public StreamTagVisitor.ValueResult visitEnd() {
        this.appendEntry(EndTag.INSTANCE);
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(String value) {
        this.appendEntry(StringTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(byte value) {
        this.appendEntry(ByteTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(short value) {
        this.appendEntry(ShortTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(int value) {
        this.appendEntry(IntTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(long value) {
        this.appendEntry(LongTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(float value) {
        this.appendEntry(FloatTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(double value) {
        this.appendEntry(DoubleTag.valueOf(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(byte[] value) {
        this.appendEntry(new ByteArrayTag(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(int[] value) {
        this.appendEntry(new IntArrayTag(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(long[] value) {
        this.appendEntry(new LongArrayTag(value));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visitList(TagType<?> elementType, int size) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.EntryResult visitElement(TagType<?> type, int index) {
        this.enterContainerIfNeeded(type);
        return StreamTagVisitor.EntryResult.ENTER;
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> type) {
        return StreamTagVisitor.EntryResult.ENTER;
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> type, String id) {
        this.containerStack.getLast().acceptKey(id);
        this.enterContainerIfNeeded(type);
        return StreamTagVisitor.EntryResult.ENTER;
    }

    private void enterContainerIfNeeded(TagType<?> type) {
        if (type == ListTag.TYPE) {
            this.containerStack.addLast(new ListBuilder());
        } else if (type == CompoundTag.TYPE) {
            this.containerStack.addLast(new CompoundBuilder());
        }
    }

    @Override
    public StreamTagVisitor.ValueResult visitContainerEnd() {
        ContainerBuilder container = this.containerStack.removeLast();
        Tag tag = container.build();
        if (tag != null) {
            this.containerStack.getLast().acceptValue(tag);
        }
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> type) {
        this.enterContainerIfNeeded(type);
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    private static class RootBuilder
    implements ContainerBuilder {
        private @Nullable Tag result;

        private RootBuilder() {
        }

        @Override
        public void acceptValue(Tag tag) {
            this.result = tag;
        }

        @Override
        public @Nullable Tag build() {
            return this.result;
        }
    }

    private static interface ContainerBuilder {
        default public void acceptKey(String id) {
        }

        public void acceptValue(Tag var1);

        public @Nullable Tag build();
    }

    private static class ListBuilder
    implements ContainerBuilder {
        private final ListTag list = new ListTag();

        private ListBuilder() {
        }

        @Override
        public void acceptValue(Tag tag) {
            this.list.addAndUnwrap(tag);
        }

        @Override
        public Tag build() {
            return this.list;
        }
    }

    private static class CompoundBuilder
    implements ContainerBuilder {
        private final CompoundTag compound = new CompoundTag();
        private String lastId = "";

        private CompoundBuilder() {
        }

        @Override
        public void acceptKey(String id) {
            this.lastId = id;
        }

        @Override
        public void acceptValue(Tag tag) {
            this.compound.put(this.lastId, tag);
        }

        @Override
        public Tag build() {
            return this.compound;
        }
    }
}

