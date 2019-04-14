package labs.lucka.mapreduce.counter

import org.apache.hadoop.io.*
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer

class SortCore {

    class CoreMapper: Mapper<Any, Text, IntWritable, Text>() {

        override fun map(key: Any?, value: Text?, context: Context?) {
            val line = value.toString()
            val pair = line.split("\\s".toRegex())
            context?.write(IntWritable(pair[1].toInt()), Text(pair[0]))
        }
    }

    class CoreReducer: Reducer<IntWritable, Text, IntWritable, Text>() {

        override fun reduce(key: IntWritable?, values: MutableIterable<Text>?, context: Context?) {
            values?.forEach { context?.write(key, it) }
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