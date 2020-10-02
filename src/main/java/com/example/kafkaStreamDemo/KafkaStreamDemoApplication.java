package com.example.kafkaStreamDemo;

import com.example.kafkaStreamDemo.counter.KStreamBindings;
import com.example.kafkaStreamDemo.counter.WordCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class KafkaStreamDemoApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamDemoApplication.class, args);
	}

	@Component
	public class StreamSource implements ApplicationRunner{

		private MessageChannel linesOut;

		public StreamSource(KStreamBindings bindings) {
			this.linesOut = bindings.linesOut();
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			List<String> stringList = Arrays.asList("skina test kafka", "test hello", "naranaiser", "hello", "from", "kafka");

			Runnable runable = ()->{
				String randomWord = stringList.get(new Random().nextInt(stringList.size()));

				Message<String> message = MessageBuilder.withPayload(randomWord)
						.setHeader(KafkaHeaders.MESSAGE_KEY, randomWord.getBytes())
						.build();
				this.linesOut.send(message);
				LOGGER.info("Sending string {}", randomWord);
			};

			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runable,1, 1,TimeUnit.SECONDS);
		}
	}

}
