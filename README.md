# Messing around with streaming JSON parsing using Akka Stream and Play Json

A small, trivial project for performing streaming JSON parsing using Akka Streams.

Once checked out, the first thing to do is to make sure everything works.

```
sbt test
```

The object `foo.StreamingJsonParse` transforms stream of bytes (represented by `Source[ByteString, Unit]`) into a 
stream of `Person` represented by `Source[Person, Unit]`.

Once a `Source[Person, Unit]` is obtained, you may choose to which sink you'd like to perform the processing, e.g. use
`runForeach` to save each instance of `Person` into database for example.

The class `foo.StreamingJsonParse.StreamingJsonParsingStage` contains the actual JSON parsing logic:
* Each instance of `foo.StreamingJsonParse.StreamingJsonParsingStage` maintains a buffer containing the byte that flows 
through the stream.
* Each byte is collected, and once the bytes can be parsed into a `Person`, the buffer is reset and the
resulting `Person` instance is pushed downstream.
 

The test `foo.StreamingJsonParseTest` illustrates the basic parsing. In this test the `runFold` sink is used to collect
each instance `Person` flowing through the stream.





