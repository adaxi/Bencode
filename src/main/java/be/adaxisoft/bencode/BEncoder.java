/**
 * Copyright (C) 2011-2012 Turn, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Adapted for distributing as a standalone library by Gerik Bonaert.
 */
package be.adaxisoft.bencode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * B-encoding encoder.
 *
 * <p>
 * This class provides utility methods to encode objects and
 * {@link BEncodedValue}s to B-encoding into a provided output stream.
 * </p>
 *
 * <p>
 * Inspired by Snark's implementation.
 * </p>
 *
 * @author mpetazzoni
 * @see <a href="http://en.wikipedia.org/wiki/Bencode">B-encoding specification</a>
 */
public class BEncoder {

	@SuppressWarnings("unchecked")
	public static void encode(Object o, OutputStream out)
		throws IOException, IllegalArgumentException {
		if (o instanceof BEncodedValue) {
			o = ((BEncodedValue)o).getValue();
		}

		if (o instanceof String) {
			encode((String)o, out);
		} else if (o instanceof byte[]) {
			encode((byte[])o, out);
		} else if (o instanceof Number) {
			encode((Number)o, out);
		} else if (o instanceof List) {
			encode((List<BEncodedValue>)o, out);
		} else if (o instanceof Map) {
			encode((Map<String, BEncodedValue>)o, out);
		} else {
			throw new IllegalArgumentException("Cannot bencode: " +
				o.getClass());
		}
	}

	public static void encode(String s, OutputStream out) throws IOException {
		byte[] bs = s.getBytes("UTF-8");
		encode(bs, out);
	}

	public static void encode(Number n, OutputStream out) throws IOException {
		out.write('i');
		String s = n.toString();
		out.write(s.getBytes("UTF-8"));
		out.write('e');
	}

	public static void encode(List<BEncodedValue> l, OutputStream out)
		throws IOException {
		out.write('l');
		for (BEncodedValue value : l) {
			encode(value, out);
		}
		out.write('e');
	}

	public static void encode(byte[] bs, OutputStream out) throws IOException {
		String l = Integer.toString(bs.length);
		out.write(l.getBytes("UTF-8"));
		out.write(':');
		out.write(bs);
	}

	public static void encode(Map<String, BEncodedValue> m, OutputStream out)
		throws IOException {
		out.write('d');

		// Keys must be sorted.
		Set<String> s = m.keySet();
		List<String> l = new ArrayList<String>(s);
		Collections.sort(l);

		for (String key : l) {
			Object value = m.get(key);
			encode(key, out);
			encode(value, out);
		}

		out.write('e');
	}

	public static ByteBuffer encode(Map<String, BEncodedValue> m)
		throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BEncoder.encode(m, baos);
		baos.close();
		return ByteBuffer.wrap(baos.toByteArray());
	}
}