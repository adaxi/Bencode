B-Encode
========

[![Build Status](https://travis-ci.org/adaxi/Bencode.svg?branch=master)](https://travis-ci.org/adaxi/Bencode) [![Coverage Status](https://coveralls.io/repos/github/adaxi/Bencode/badge.svg?branch=master)](https://coveralls.io/github/adaxi/Bencode?branch=master)

This library allows you to encode and decode B-encoded documents.

Before:

```java
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
String encodedDocument = new String(baos.toByteArray());
```

After:

```
d
	4:dict		d
					3:123	4:test
					3:456	5:thing
				e
	4:list		l
					11:list-item-1
					11:list-item-2
				e
	6:number	i123456e
	6:string	5:value
e
```

The library can do the reverse operation as well: decode.


Use the library
---------------

The [documentation page](http://adaxi.github.io/Bencode/dependency-info.html) lists how you can add this library
as a dependency of your project.

Credit
------

This library was created since no other viable libraries existed, and the ttorrent
project contains a bunch of unecessary content. It is based on the work of [Maxime Petazzoni](https://github.com/mpetazzoni/ttorrent).
