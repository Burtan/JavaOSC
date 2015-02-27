/*
 * Copyright (C) 2015, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.argument.handler;

import com.illposed.osc.argument.ArgumentHandler;
import com.illposed.osc.OSCParseException;
import com.illposed.osc.OSCSerializeException;
import com.illposed.osc.SizeTrackingOutputStream;
import java.awt.Color;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Parses and serializes an OSC RGBA color type (32bit).
 */
public class ColorArgumentHandler implements ArgumentHandler<Color>, Cloneable {

	public static final ArgumentHandler<Color> INSTANCE = new ColorArgumentHandler();

	/** Allow overriding, but somewhat enforce the ugly singleton. */
	protected ColorArgumentHandler() {
		// ctor declared only for setting the access level
	}

	@Override
	public char getDefaultIdentifier() {
		return 'r';
	}

	@Override
	public Class<Color> getJavaClass() {
		return Color.class;
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
	public ColorArgumentHandler clone() throws CloneNotSupportedException {
		return (ColorArgumentHandler) super.clone();
	}

	/**
	 * Converts the argument to an {@code int} by an unsigned conversion.
	 *
	 * @param signedByte the value to convert to an unsigned {@code int}
	 * @return the argument converted to {@code int} by an unsigned conversion NOTE Since Java 8,
	 * one could use Byte#toUnsignedInt
	 */
	public static int toUnsignedInt(final byte signedByte) {
		return ((int) signedByte) & 0xff;
	}

	/**
	 * Converts the argument to an {@code byte} by a sign introducing conversion.
	 *
	 * @param unsignedInt the value to convert to a signed {@code byte}; has to be in range [0, 255]
	 * @return the argument converted to {@code byte} by a sign introducing conversion
	 */
	public static byte toSignedByte(final int unsignedInt) {
		return (byte) unsignedInt;
	}

	@Override
	public Color parse(final ByteBuffer input) throws OSCParseException {
		return new Color(
				toUnsignedInt(input.get()),
				toUnsignedInt(input.get()),
				toUnsignedInt(input.get()),
				toUnsignedInt(input.get()));
	}

	@Override
	public void serialize(final SizeTrackingOutputStream stream, final Color value)
			throws IOException, OSCSerializeException
	{
		stream.write(toSignedByte(value.getRed()));
		stream.write(toSignedByte(value.getGreen()));
		stream.write(toSignedByte(value.getBlue()));
		stream.write(toSignedByte(value.getAlpha()));
	}
}
