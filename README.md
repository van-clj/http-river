# http-river

This library provides river producers that performs HTTP requests
and streams their response either as a seq of characters or a seq
of bytes.

The functions available are:

* produce-http-get
* produce-http-get-bytes
* produce-http-post
* produce-http-post-bytes

For more info about how river works, check the
[river API](http://github.com/roman/river)

## Install

[org.van-clj/http-river "0.0.1"]

## License

Copyright (C) 2012 Roman Gonzalez.

Distributed under the Eclipse Public License, the same as Clojure.
