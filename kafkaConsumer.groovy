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

String brokers = "localhost:9092";
String topic = "demo";
Properties kafkaProps = new Properties();
kafkaProps.put("bootstrap.servers", brokers);
kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
kafkaProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
kafkaProps.put("group.id", "testGroup");
kafkaProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
Duration timeout = Duration.ofMillis(100);


KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(kafkaProps);
consumer.subscribe(Collections.singletonList("demo"));	


try {
	println("I am listening to the event from server:" + brokers + ",topic:" + topic);
	while (true) {
		ConsumerRecords<String, String> records = consumer.poll(timeout);
		for (ConsumerRecord<String, String> record: records) {
			Timestamp ts = new Timestamp(record.timestamp());  
			Date date=new Date(ts.getTime());  
			println("------------------------------------------------------");
			println("record timestamp: " + record.timestamp());
			println("record date     : " + date);
			println("record key      : " + record.key());
			println("record partition: " + record.partition());
			println("record offset   : " + record.offset());
			println("record value    : " + record.value());
			println("------------------------------------------------------");
		}
	}
} catch (Exception ex) {
	println("Polling exception: " + ex.getMessage());
}
