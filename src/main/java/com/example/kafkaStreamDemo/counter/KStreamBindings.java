package com.example.kafkaStreamDemo.counter;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KStreamBindings {
	String LINES_OUT="linesOut";
	String LINES_IN="linesIn";
	String WORD_COUNT_OUT="wcout";
	String WORD_COUNT_IN="wcin";
	String WORD_COUNT_ST="test-word-counts";

	@Output(LINES_OUT)
	MessageChannel linesOut();

	@Input(LINES_IN)
	KStream<?, ?> lines2Read();

	@Output(WORD_COUNT_OUT)
	KStream<?, ?> wordsCounted();

	@Input(WORD_COUNT_IN)
	KStream<?, ?> wordsCountedIn();
}
