# mapreduce-counter-kotlin

A Hadoop MapReduce implement for word counting, written in Kotlin with IntelliJ IDEA.

The `.jar` file generated by IJ will not include Hadoop libraries.

## Requirement
- Library (`.jar` files) of Apache Hadoop 3.2.0

## Build
1. Add `HADOOP_HOME` in `Preferences - Appearance & Behavior - Path Variables`
2. Build artifacts, `.jar` will be generated to `out/artifacts/mapreduce_counter_kotlin_jar/`
3. Send the `.jar` to Hadoop server and run it