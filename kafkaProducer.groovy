@Grapes(
    @Grab(group='org.apache.kafka', module='kafka-clients', version='3.3.1')
)
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.TimeUnit;

//String brokers = "localhost:29091,localhost:29092,localhost:29093";
String brokers = "localhost:9092";
String topic = "demo";
String key = "just a key";
String value = "just a message";

Properties kafkaProps = new Properties();
kafkaProps.put("bootstrap.servers", brokers);
kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

Producer<String, String> producer = new KafkaProducer<>(kafkaProps);
try {
	producer.send(new ProducerRecord<String, String>(topic, key, value)).get();
	println("Message sent with below information:");
    println("server :: " + brokers);
    println("topic  :: " + topic);
    println("key    :: " + key);
    println("value  :: " + value);
} finally {
	producer.close();
}