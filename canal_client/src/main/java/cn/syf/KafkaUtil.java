package cn.syf;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaUtil {
    public static void sendData(String topic,String data){
        Producer<String,String> producer = getProducer();
        producer.send(new ProducerRecord<>(topic,data));
        producer.flush();

    }
    private static Producer<String,String> getProducer(){
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers","spring:9092,summer:9092,autumn:9092");
        properties.setProperty("acks","1");
        properties.setProperty("retries","0");
        properties.setProperty("batch.size","16384");
        properties.setProperty("linger.ms","1");
        properties.setProperty("buffer.memory","33554432");
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return  new KafkaProducer<>(properties);
    }
}
