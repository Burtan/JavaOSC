/*
 * Copyright (C) 2015, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.argument.handler;

import com.illposed.osc.OSCParseException;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.argument.ArgumentHandler;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Parses and serializes an OSC long type (64bit signed integer).
 */
public class LongArgumentHandler implements ArgumentHandler<Long>, Cloneable {

	/**
	 * The number of bytes used to represent this type in an OSC byte array (8).
	 */
	public static final int BYTES = Long.SIZE / Byte.SIZE;
	public static final ArgumentHandler<Long> INSTANCE = new LongArgumentHandler();

	/** Allow overriding, but somewhat enforce the ugly singleton. */
	protected LongArgumentHandler() {
		// ctor declared only for setting the access level
	}

	@Override
	public char getDefaultIdentifier() {
		return 'h';
	}

	@Override
	public Class<Long> getJavaClass() {
		return Long.class;
	}

	@Override
	public void setProperties(final Map<String, Object> properties) {
		// we make no use of any properties
	}

	@Override
	public boolean isMarkerOnly() {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public LongArgumentHandler clone() throws CloneNotSupportedException {
		return (LongArgumentHandler) super.clone();
	}

	@Override
	public Long parse(final ByteBuffer input) throws OSCParseException {

		final Long value = input.asLongBuffer().get();
		input.position(input.position() + BYTES);
		return value;
	}

	@Override
	public void serialize(final ByteBuffer output, final Long value) throws OSCSerializeException {

		long curValue = value;
		final byte[] longintBytes = new byte[8];
		longintBytes[7] = (byte)curValue; curValue >>>= 8;
		longintBytes[6] = (byte)curValue; curValue >>>= 8;
		longintBytes[5] = (byte)curValue; curValue >>>= 8;
		longintBytes[4] = (byte)curValue; curValue >>>= 8;
		longintBytes[3] = (byte)curValue; curValue >>>= 8;
		longintBytes[2] = (byte)curValue; curValue >>>= 8;
		longintBytes[1] = (byte)curValue; curValue >>>= 8;
		longintBytes[0] = (byte)curValue;

		output.put(longintBytes);
	}
}
