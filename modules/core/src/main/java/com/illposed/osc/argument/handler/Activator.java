/*
 * Copyright (C) 2015, C. Ramakrishnan / Illposed Software.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.argument.handler;

import com.illposed.osc.argument.ArgumentHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to easily register and unregister all types in this package.
 */
public final class Activator {

	private static final List<ArgumentHandler> TYPES_STATIC_COMMON;
	static {
		final ArrayList<ArgumentHandler> types = new ArrayList<ArgumentHandler>();
		types.add(BlobArgumentHandler.INSTANCE);
		types.add(BooleanFalseArgumentHandler.INSTANCE);
		types.add(BooleanTrueArgumentHandler.INSTANCE);
		types.add(CharArgumentHandler.INSTANCE);
		types.add(ColorArgumentHandler.INSTANCE);
		types.add(DoubleArgumentHandler.INSTANCE);
		types.add(FloatArgumentHandler.INSTANCE);
		types.add(ImpulseArgumentHandler.INSTANCE);
		types.add(IntegerArgumentHandler.INSTANCE);
		types.add(LongArgumentHandler.INSTANCE);
		types.add(MidiMessageArgumentHandler.INSTANCE);
		types.add(NullArgumentHandler.INSTANCE);
		types.add(TimeStampArgumentHandler.INSTANCE);
		types.add(UnsignedIntegerArgumentHandler.INSTANCE);
		types.trimToSize();
		TYPES_STATIC_COMMON = Collections.unmodifiableList(types);
	}

	private Activator() {}

	public static Map<Character, ArgumentHandler> createParserTypes() {

		final Map<Character, ArgumentHandler> parserTypes
				= new HashMap<Character, ArgumentHandler>(TYPES_STATIC_COMMON.size() + 1);
		for (final ArgumentHandler type : TYPES_STATIC_COMMON) {
			parserTypes.put(type.getDefaultIdentifier(), type);
		}

		final StringArgumentHandler stringArgumentHandler = new StringArgumentHandler();
		parserTypes.put(stringArgumentHandler.getDefaultIdentifier(), stringArgumentHandler);

		final SymbolArgumentHandler symbolArgumentHandler = new SymbolArgumentHandler();
		parserTypes.put(symbolArgumentHandler.getDefaultIdentifier(), symbolArgumentHandler);

		// NOTE We do not register DateTimeStampArgumentHandler here,
		//   because type 't' already converts to OSCTimeStamp.

		return parserTypes;
	}

	public static List<ArgumentHandler> createSerializerTypes() {

		final List<ArgumentHandler> serializerTypes
				= new ArrayList<ArgumentHandler>(TYPES_STATIC_COMMON.size() + 2);
		serializerTypes.addAll(TYPES_STATIC_COMMON);

		final StringArgumentHandler stringArgumentHandler = new StringArgumentHandler();
		serializerTypes.add(stringArgumentHandler);

		final SymbolArgumentHandler symbolArgumentHandler = new SymbolArgumentHandler();
		serializerTypes.add(symbolArgumentHandler);

		// NOTE We add this for legacy suppport, though it is recommended
		//   to use OSCTimeStamp over Date, to not loose precission and range during conversions.
		final ArgumentHandler dateArgumentHandler = DateTimeStampArgumentHandler.INSTANCE;
		serializerTypes.add(dateArgumentHandler);

		return serializerTypes;
	}
}
