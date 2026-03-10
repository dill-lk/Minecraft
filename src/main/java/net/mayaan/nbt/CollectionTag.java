/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.nbt;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.mayaan.nbt.ByteArrayTag;
import net.mayaan.nbt.IntArrayTag;
import net.mayaan.nbt.ListTag;
import net.mayaan.nbt.LongArrayTag;
import net.mayaan.nbt.Tag;

public sealed interface CollectionTag
extends Tag,
Iterable<Tag>
permits ListTag, ByteArrayTag, IntArrayTag, LongArrayTag {
    public void clear();

    public boolean setTag(int var1, Tag var2);

    public boolean addTag(int var1, Tag var2);

    public Tag remove(int var1);

    public Tag get(int var1);

    public int size();

    default public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    default public Iterator<Tag> iterator() {
        return new Iterator<Tag>(this){
            private int index;
            final /* synthetic */ CollectionTag this$0;
            {
                CollectionTag collectionTag = this$0;
                Objects.requireNonNull(collectionTag);
                this.this$0 = collectionTag;
            }

            @Override
            public boolean hasNext() {
                return this.index < this.this$0.size();
            }

            @Override
            public Tag next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return this.this$0.get(this.index++);
            }
        };
    }

    default public Stream<Tag> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}

