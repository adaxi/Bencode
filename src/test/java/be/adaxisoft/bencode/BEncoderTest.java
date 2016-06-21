package be.adaxisoft.bencode;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

public class BEncoderTest {

	@SuppressWarnings("serial")
	@Test
	public void encode() throws IOException {

		String bencodedString =
				  "d4:dictd3:1234:test3:4565:thinge"
				+ "4:listl11:list-item-111:list-item-2e"
				+ "6:numberi123456e"
				+ "6:string5:valuee";

		Map<String,BEncodedValue> document = new HashMap<String, BEncodedValue>() {{
		    put("string", new BEncodedValue("value"));
		    put("number", new BEncodedValue(123456));
		    put("list", new BEncodedValue(new ArrayList<BEncodedValue>() {{
		        add(new BEncodedValue("list-item-1"));
		        add(new BEncodedValue("list-item-2"));
		    }}));
		    put("dict", new BEncodedValue(new HashMap<String, BEncodedValue>() {{
		        put("123", new BEncodedValue("test"));
		        put("456", new BEncodedValue("thing"));
		    }}));
		}};

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BEncoder.encode(document, baos);
		byte[] encodedDocument = baos.toByteArray();

		assertEquals(new String(encodedDocument), bencodedString);
	}

	@Test
	public void decode() throws IOException {

		String bencodedString =
				  "d4:dictd3:1234:test3:4565:thinge"
				+ "4:listl11:list-item-111:list-item-2e"
				+ "6:numberi123456e"
				+ "6:string5:valuee";

		ByteArrayInputStream bais = new ByteArrayInputStream(bencodedString.getBytes());
		BDecoder decoder = new BDecoder(bais);
		BEncodedValue value = decoder.decodeMap();
		Map<String,BEncodedValue> document = value.getMap();

		assertEquals("value", document.get("string").getString());
		assertEquals(123456, document.get("number").getLong());

		List<BEncodedValue> list = document.get("list").getList();

		assertEquals("list-item-1", list.get(0).getString());
		assertEquals("list-item-2", list.get(1).getString());

		Map<String,BEncodedValue> map = document.get("dict").getMap();
		assertEquals("test", map.get("123").getString());
		assertEquals("thing", map.get("456").getString());
	}

}
