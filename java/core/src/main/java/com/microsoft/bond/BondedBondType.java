// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.microsoft.bond;

import java.io.IOException;
import java.util.HashMap;

/**
 * Implements the {@link BondType} contract for bonded container data type.
 * @param <TStruct> the class of the underlying struct value
 */
public final class BondedBondType<TStruct extends BondSerializable> extends BondType<IBonded<TStruct>> {

    /**
     * The name of the type as it appears in Bond schemas.
     */
    public static final String TYPE_NAME = "bonded";

    private final StructBondType<TStruct> valueType;
    private final int precomputedHashCode;

    BondedBondType(StructBondType<TStruct> valueType) {
        this.valueType = valueType;
        this.precomputedHashCode = HashCode.computeHashCodeForBondedContainer(valueType);
    }

    /**
     * Retrieves the underlying value type descriptor.
     *
     * @return the underlying value type descriptor
     */
    public final BondType<TStruct> getValueType() {
        return this.valueType;
    }

    @Override
    public final String getName() {
        return TYPE_NAME;
    }

    @Override
    public final String getQualifiedName() {
        return TYPE_NAME;
    }

    @Override
    public final BondDataType getBondDataType() {
        return this.valueType.getBondDataType();
    }

    @Override
    public final Class<IBonded<TStruct>> getValueClass() {
        // can't do direct cast
        @SuppressWarnings("unchecked")
        Class<IBonded<TStruct>> valueClass = (Class<IBonded<TStruct>>) (Class<?>) IBonded.class;
        return valueClass;
    }

    @Override
    public final Class<IBonded<TStruct>> getPrimitiveValueClass() {
        return null;
    }

    @Override
    public final boolean isNullableType() {
        return false;
    }

    @Override
    public final boolean isGenericType() {
        return true;
    }

    @Override
    public final BondType<?>[] getGenericTypeArguments() {
        return new BondType<?>[]{this.valueType};
    }

    @Override
    protected final Bonded<TStruct> newDefaultValue() {
        return new Bonded<TStruct>(this.valueType.newDefaultValue());
    }

    @Override
    protected final void serializeValue(SerializationContext context, IBonded<TStruct> value) throws IOException {
        this.verifyNonNullableValueIsNotSetToNull(value);

        // TODO: complete serialization story for bonded
        throw new UnsupportedOperationException();
    }

    @Override
    protected final Bonded<TStruct> deserializeValue(TaggedDeserializationContext context) throws IOException {
        TStruct value = this.valueType.deserializeValue(context);

        // TODO: complete deserialization story for bonded
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void serializeField(
            SerializationContext context,
            IBonded<TStruct> value,
            StructBondType.StructField<IBonded<TStruct>> field) throws IOException {
        // struct (bonded) fields are never omitted
        context.writer.writeFieldBegin(BondDataType.BT_STRUCT, field.getId(), field);
        try {
            this.serializeValue(context, value);
        } catch (InvalidBondDataException e) {
            // throws
            Throw.raiseStructFieldSerializationError(false, field, e, null);
        }
        context.writer.writeFieldEnd();
    }

    @Override
    protected final IBonded<TStruct> deserializeField(
            TaggedDeserializationContext context,
            StructBondType.StructField<IBonded<TStruct>> field) throws IOException {
        // since bonded applies only to structs, a bonded value may be deserialized only from BT_STRUCT
        if (context.readFieldResult.type.value != BondDataType.BT_STRUCT.value) {
            // throws
            Throw.raiseFieldTypeIsNotCompatibleDeserializationError(context.readFieldResult.type, field);
        }
        Bonded<TStruct> value = null;
        try {
            value = this.deserializeValue(context);
        } catch (InvalidBondDataException e) {
            // throws
            Throw.raiseStructFieldSerializationError(true, field, e, null);
        }
        return value;
    }

    @Override
    public final int hashCode() {
        return this.precomputedHashCode;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof BondedBondType<?>) {
            BondedBondType<?> that = (BondedBondType<?>) obj;
            return this.precomputedHashCode == that.precomputedHashCode &&
                    this.valueType.equals(that.valueType);
        } else {
            return false;
        }
    }

    @Override
    final TypeDef createSchemaTypeDef(HashMap<StructBondType<?>, StructDefOrdinalTuple> structDefMap) {
        TypeDef typeDef = this.valueType.createSchemaTypeDef(structDefMap);
        typeDef.bonded_type = true;
        return typeDef;
    }
}