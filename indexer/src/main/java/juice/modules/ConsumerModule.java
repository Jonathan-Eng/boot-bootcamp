package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigurationFactory;
import config.ConsumerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "consumer.config";


    @Provides
    public KafkaConsumer<String, String> provideConsumer() {
        ConsumerConfiguration cc = ConfigurationFactory.create(
                CONFIG_FILE_NAME,
                ConsumerConfiguration.class);

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cc.getHost() + ":" + cc.getPort());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, cc.getGroupId());

        return new KafkaConsumer<>(props);
    }

}
