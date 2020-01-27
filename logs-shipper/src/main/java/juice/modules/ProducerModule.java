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
    private final ProducerConfiguration producerConfiguration;

    public ProducerModule() {
        this.producerConfiguration = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ProducerConfiguration.class
        );
    }

    @Provides
    ProducerConfiguration provideProducerConfiguration() {
        return this.producerConfiguration;
    }

    @Provides
    KafkaProducer<String, String> getKafkaProducer() {

        // init kafka properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfiguration.getHost() + ":" + producerConfiguration.getPort());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producerConfiguration.getClientId());

        // create producer
        return new KafkaProducer<String, String>(props);
    }

}
