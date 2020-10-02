package com.example.kafkaStreamDemo.counter;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author: piritz
 */
@Component
public class PageCountSink {
	private static final Logger LOGGER = LoggerFactory.getLogger(PageCountSink.class);

	@StreamListener(KStreamBindings.WORD_COUNT_IN)
	public void counterSink( KStream<String, Long> counter){
		counter.foreach((key, value) -> LOGGER.info("Received record key: {} and value:{}", key, value));
	}
}
