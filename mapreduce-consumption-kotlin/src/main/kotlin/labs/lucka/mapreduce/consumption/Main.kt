package labs.lucka.mapreduce.consumption

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.hadoop.MongoInputFormat
import com.mongodb.hadoop.MongoOutputFormat
import com.mongodb.hadoop.util.MongoClientURIBuilder
import com.mongodb.hadoop.util.MongoConfigUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.DoubleWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job

fun main(args: Array<String>) {

    // Test the connection of mongoDB server
    //testConnection("SERVER.DOMAIN", "DATABASE_NAME")
    //System.exit(0)

    if (args.size < 4) System.exit(1)
    val mongoHost = args[0]
    val mongoDatabase = args[1]
    val mongoInputCollection = args[2]
    val mongoOutputCollection = args[3]

    val mongoClientOptions = MongoClientOptions.builder().sslEnabled(true).build()
    val mongoInputURI = MongoClientURIBuilder()
        .host(mongoHost)
        .collection(mongoDatabase, mongoInputCollection)
        .options(mongoClientOptions)
        .build()
    val mongoOutputURI = MongoClientURIBuilder()
        .host(mongoHost)
        .collection(mongoDatabase, mongoOutputCollection)
        .options(mongoClientOptions)
        .build()

    val conf = Configuration()
    MongoConfigUtil.setInputURI(conf, mongoInputURI)
    MongoConfigUtil.setOutputURI(conf, mongoOutputURI)

    val priceJob = Job.getInstance(conf, "Mongo: Price")
    priceJob.setJarByClass(PriceCore::class.java)
    priceJob.inputFormatClass = MongoInputFormat::class.java
    priceJob.mapperClass = PriceCore.CoreMapper::class.java
    priceJob.reducerClass = PriceCore.CoreReducer::class.java
    priceJob.outputKeyClass = Text::class.java
    priceJob.outputValueClass = DoubleWritable::class.java
    priceJob.outputFormatClass = MongoOutputFormat::class.java

    System.exit(if(priceJob.waitForCompletion(true)) 0 else 1)
}

fun testConnection(host: String, databaseName: String) {
    val mongoURI = MongoClientURIBuilder()
        .host(host)
        .options(MongoClientOptions.builder().sslEnabled(true).build())
        .build()
    val client = MongoClient(mongoURI)
    val db = client.getDatabase(databaseName)
    print(db.name)
}