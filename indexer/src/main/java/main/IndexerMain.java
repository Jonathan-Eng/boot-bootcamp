package main;

import com.google.inject.Guice;
import consumer.IndexerConsumer;
import juice.modules.IndexerModule;
import juice.modules.StrictExplicitBindingModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexerMain {
    private static final Logger logger = LogManager.getLogger(IndexerMain.class);

    public static void main(String[] args) {
        Guice.createInjector(new IndexerModule(), new StrictExplicitBindingModule())
                .getInstance(IndexerConsumer.class).consume();
        logger.info("Consumer is running");
    }
}
