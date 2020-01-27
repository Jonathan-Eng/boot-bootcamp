package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ConsumerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class KafkaConsumerModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "consumer.config";
    private final ConsumerConfiguration consumerConfiguration;

    public KafkaConsumerModule(){
        consumerConfiguration = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ConsumerConfiguration.class);
    }

    @Provides
    public ConsumerConfiguration provideConsumerConfiguration() {
        return consumerConfiguration;
    }

    @Provides
    public KafkaConsumer<String, String> provideConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfiguration.getHost() + ":" + consumerConfiguration.getPort());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerConfiguration.getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new KafkaConsumer<>(props);
    }

}
