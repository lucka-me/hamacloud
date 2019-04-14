package labs.lucka.mapreduce.counter

import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import java.util.*

class CountCore {

    class CoreMapper: Mapper<Any, Text, Text, IntWritable>() {

        override fun map(key: Any?, value: Text?, context: Context?) {
            val iterator = StringTokenizer(value.toString())
            while (iterator.hasMoreTokens()) {
                WORD.set(iterator.nextToken())
                context?.write(
                    WORD,
                    ONE
                )
            }
        }

        companion object {
            private val WORD = Text()
            private val ONE = IntWritable(1)
        }
    }

    class CoreReducer: Reducer<Text, IntWritable, Text, IntWritable>() {

        override fun reduce(key: Text?, values: MutableIterable<IntWritable>?, context: Context?) {
            context?.write(key, IntWritable(values?.sumBy { it.get() } ?: return))
        }

    }
}