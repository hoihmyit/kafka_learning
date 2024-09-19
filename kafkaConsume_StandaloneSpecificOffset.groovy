// https://developer.confluent.io/tutorials/kafka-console-consumer-read-specific-offsets-partitions/confluent.html#:~:text=Short%20Answer,a%20specific%20partition%20and%20offset.
@Grapes(
    @Grab(group='org.apache.kafka', module='kafka-clients', version='3.3.1')
)

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.util.*;
import java.sql.Timestamp;   

//String brokers = "localhost:29091,localhost:29092,localhost:29093";
String brokers = "localhost:9092";
String topic = "demo";
Properties kafkaProps = new Properties();
kafkaProps.put("bootstrap.servers", brokers);
kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

int partition = 0;
long offset = 1;
int numberOfMessagesToRead = 2;

Consumer<String, String> consumer;
try {
	consumer = new KafkaConsumer<>(kafkaProps);
	partitionInfos = consumer.partitionsFor(topic);

} catch(Exception e) {
	println("Could not get the Kafka configuration for Refresh Config listener!");
}

// Assign the partition and seek to the desired offset
TopicPartition topicPartition = new TopicPartition(topic, partition);
consumer.assign(Collections.singletonList(topicPartition));
consumer.seek(topicPartition, offset);

boolean keepOnReading = true;
int numberOfMessagesReadSoFar = 0;
try {
	println("Listen to the event from server:" + brokers + ",topic:" + topic);
	Duration timeout = Duration.ofMillis(1000);
	while(keepOnReading) {
		println("Waiting ...");
		ConsumerRecords<String, String> records = consumer.poll(timeout);
		for (ConsumerRecord<String, String> record: records) {
			numberOfMessagesReadSoFar++;
			Timestamp ts = new Timestamp(record.timestamp());  
			Date date = new Date(ts.getTime());  
			println("------------------------------------------------------");
			println("record timestamp: " + record.timestamp());
			println("record date     : " + date);
			println("record key      : " + record.key());
			println("record partition: " + record.partition());
			println("record offset   : " + record.offset());
			println("record value    : " + record.value());
			println("------------------------------------------------------");
			if (numberOfMessagesReadSoFar >= numberOfMessagesToRead){
				keepOnReading = false; // to exit the while loop
				break; // to exit the for loop
			}
		}
	}
} catch (Exception ex) {
	println("Catching the exception: " + ex.getMessage());
} finally {
	println("Finally, done!")
	consumer.close();
}
