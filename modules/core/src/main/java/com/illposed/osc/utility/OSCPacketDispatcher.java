/*
 * Copyright (C) 2003, C. Ramakrishnan / Auracle.
 * All rights reserved.
 *
 * This code is licensed under the BSD 3-Clause license.
 * See file LICENSE (or LICENSE.html) for more information.
 */

package com.illposed.osc.utility;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

/**
 * Dispatches OSCMessages to registered listeners.
 *
 * @author Chandrasekhar Ramakrishnan
 */

public class OSCPacketDispatcher {
	// use Hashtable for JDK 1.1 compatability
	private Hashtable addressToClassTable = new Hashtable();

	/**
	 *
	 */
	public OSCPacketDispatcher() {
		super();
	}

	public void addListener(String address, OSCListener listener) {
		addressToClassTable.put(address, listener);
	}

	public void dispatchPacket(OSCPacket packet) {
		if (packet instanceof OSCBundle) {
			dispatchBundle((OSCBundle) packet);
		} else {
			dispatchMessage((OSCMessage) packet);
		}
	}

	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle) {
			dispatchBundle((OSCBundle) packet);
		} else {
			dispatchMessage((OSCMessage) packet, timestamp);
		}
	}

	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		OSCPacket[] packets = bundle.getPackets();
		for (int i = 0; i < packets.length; i++) {
			dispatchPacket(packets[i], timestamp);
		}
	}

	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}

	private void dispatchMessage(OSCMessage message, Date time) {
		Enumeration keys = addressToClassTable.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			// this supports the OSC regexp facility, but it
			// only works in JDK 1.4, so don't support it right now
			// if (key.matches(message.getAddress())) {
			if (key.equals(message.getAddress())) {
				OSCListener listener
						= (OSCListener) addressToClassTable.get(key);
				listener.acceptMessage(time, message);
			}
		}
	}
}