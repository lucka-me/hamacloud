package labs.lucka.mapreduce.counter

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

fun main(args: Array<String>) {
    if (args.size < 2) System.exit(1)

    val conf = Configuration()

    val hdfs = FileSystem.get(conf)
    val inputPath = Path(args[0])
    val outputPath = Path(args[1])
    if (hdfs.exists(outputPath)) hdfs.delete(outputPath, true)

    // The intermediate folder for count job
    val intermediatePath = Path("/tmp/labs.lucka.mapreduce.counter")
    if (hdfs.exists(intermediatePath)) hdfs.delete(intermediatePath, true)

    // Count Job
    val jobCount = Job.getInstance(conf, "Counter: Word Count")
    jobCount.setJarByClass(CountCore::class.java)
    jobCount.mapperClass = CountCore.CoreMapper::class.java
    jobCount.combinerClass = CountCore.CoreReducer::class.java
    jobCount.reducerClass = CountCore.CoreReducer::class.java
    jobCount.outputKeyClass = Text::class.java
    jobCount.outputValueClass = IntWritable::class.java

    FileInputFormat.addInputPath(jobCount, inputPath)
    FileOutputFormat.setOutputPath(jobCount, intermediatePath)
    if (!jobCount.waitForCompletion(true)) System.exit(1)

    // Delete the _SUCCESS file
    val intermediateResultFlagPath = Path(intermediatePath, "_SUCCESS")
    hdfs.delete(intermediateResultFlagPath, true)

    // Sort Job
    val jobSort = Job.getInstance(conf, "Counter: Sort")
    jobSort.setJarByClass(SortCore::class.java)
    jobSort.mapperClass = SortCore.CoreMapper::class.java
    jobSort.setSortComparatorClass(SortCore.CoreComparator::class.java)
    jobSort.reducerClass = SortCore.CoreReducer::class.java
    jobSort.outputKeyClass = IntWritable::class.java
    jobSort.outputValueClass = Text::class.java

    FileInputFormat.addInputPath(jobSort, intermediatePath)
    FileOutputFormat.setOutputPath(jobSort, outputPath)
    if (!jobSort.waitForCompletion(true)) System.exit(1)

    // Delete the intermediate folder
    hdfs.delete(intermediatePath, true)
    System.exit(0)
}