package labs.lucka.mapreduce.consumption

import org.apache.hadoop.io.DoubleWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.bson.BSONObject

class PriceCore {

    class CoreMapper: Mapper<Any, BSONObject, Text, DoubleWritable>() {

        override fun map(key: Any?, value: BSONObject?, context: Context?) {
            if (value == null) return
            val price = (value.get("price") as String).toDoubleOrNull() ?: return
            val area = (value.get("province") as String) + (value.get("city") as String) + (value.get("area") as String)
            context?.write(Text(area), DoubleWritable(price))
        }

    }

    class CoreReducer: Reducer<Text, DoubleWritable, Text, DoubleWritable>() {

        override fun reduce(key: Text?, values: MutableIterable<DoubleWritable>?, context: Context?) {
            if (values == null) return
            var sum = 0.0
            var count = 0
            values.forEach {
                sum += it.get()
                count++
            }
            context?.write(key, DoubleWritable(sum / count))
        }

    }

}