package labs.lucka.mapreduce.counter

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

fun main(args: Array<String>) {
    if (args.size < 2) System.exit(1)

    val conf = Configuration()
    val job = Job.getInstance(conf, "Word Count")
    job.setJarByClass(CounterCore::class.java)
    job.mapperClass = CounterCore.CoreMapper::class.java
    job.combinerClass = CounterCore.CoreReducer::class.java
    job.reducerClass = CounterCore.CoreReducer::class.java
    job.outputKeyClass = Text::class.java
    job.outputValueClass = IntWritable::class.java

    FileInputFormat.addInputPath(job, Path(args[0]))
    FileOutputFormat.setOutputPath(job, Path(args[1]))
    System.exit(if(job.waitForCompletion(true)) 0 else 1)
}