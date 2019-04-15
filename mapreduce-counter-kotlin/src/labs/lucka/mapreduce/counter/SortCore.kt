package labs.lucka.mapreduce.counter

import org.apache.hadoop.io.*
import org.apache.hadoop.mapreduce.*
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.input.FileSplit
import org.apache.hadoop.util.LineReader

class SortCore {

    class CoreMapper: Mapper<Text, IntWritable, IntWritable, Text>() {

        override fun map(key: Text?, value: IntWritable?, context: Context?) {
            context?.write(value, key)
        }

    }

    class CoreReducer: Reducer<IntWritable, Text, IntWritable, Text>() {

        override fun reduce(key: IntWritable?, values: MutableIterable<Text>?, context: Context?) {
            values?.forEach { context?.write(key, it) }
        }

    }

    class CoreInputFormat: FileInputFormat<Text, IntWritable>() {

        override fun createRecordReader(p0: InputSplit?, p1: TaskAttemptContext?): RecordReader<Text, IntWritable> =
            CoreRecordReader()

    }

    private class CoreRecordReader: RecordReader<Text, IntWritable>() {

        private lateinit var reader: LineReader
        private lateinit var currentKey: Text
        private lateinit var currentValue: IntWritable

        override fun initialize(input: InputSplit?, context: TaskAttemptContext?) {
            if (input == null || context == null) throw Exception("Something wrong")
            input as FileSplit
            val conf = context.configuration
            val path = input.path
            val hdfs = path.getFileSystem(conf)
            val inputStream = hdfs.open(path)
            reader = LineReader(inputStream, conf)
            currentKey = Text()
            currentValue = IntWritable()
        }

        override fun getCurrentKey(): Text = currentKey

        override fun getCurrentValue(): IntWritable = currentValue

        override fun getProgress(): Float = 0F

        override fun nextKeyValue(): Boolean {
            val line = Text()
            val size = reader.readLine(line)
            if (size == 0) return false
            val pair = line.toString().split("\\s".toRegex())
            currentKey = Text(pair[0])
            currentValue = IntWritable(pair[1].toInt())
            return true
        }

        override fun close() {
            reader.close()
        }

    }

    class CoreComparator: IntWritable.Comparator() {

        override fun compare(b1: ByteArray?, s1: Int, l1: Int, b2: ByteArray?, s2: Int, l2: Int): Int {
            return -1 * super.compare(b1, s1, l1, b2, s2, l2)
        }

        override fun compare(a: Any?, b: Any?): Int {
            return -1 * super.compare(a, b)
        }

        override fun compare(a: WritableComparable<*>?, b: WritableComparable<*>?): Int {
            return -1 * super.compare(a, b)
        }

    }

}