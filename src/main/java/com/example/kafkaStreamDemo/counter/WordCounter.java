package com.example.kafkaStreamDemo.counter;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.example.kafkaStreamDemo.counter.KStreamBindings.WORD_COUNT_ST;

/**
 * @author: piritz
 */
@Component
@EnableBinding(KStreamBindings.class)
public class WordCounter {

	public static final Logger LOGGER = LoggerFactory.getLogger(WordCounter.class);


	public WordCounter() {
	}

	@StreamListener(KStreamBindings.LINES_IN)
	@SendTo(KStreamBindings.WORD_COUNT_OUT)
	public KStream<String, Long> countWord(KStream<String, String> textLines){
		Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

		return textLines.flatMapValues(value -> Arrays.asList(pattern.split(value)))
				.groupBy((key, word) -> word, Grouped.with(new Serdes.StringSerde(), new Serdes.StringSerde()))
				.count(Materialized.<String, Long>as(Stores.persistentKeyValueStore(WORD_COUNT_ST))
					.withRetention(Duration.ofSeconds(10))
						.withKeySerde(new Serdes.StringSerde())
						.withValueSerde(new Serdes.LongSerde())
				).toStream()
				;


	}


}
