package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ProducerConfiguration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "producer.config";

    @Provides
    KafkaProducer<String, String> getKafkaProducer() {

        ProducerConfiguration pc = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ProducerConfiguration.class
        );

        // init kafka properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, pc.getHost() + ":" + pc.getPort());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, pc.getClientId());

        // create producer
        return new KafkaProducer<String, String>(props);

    }

}
