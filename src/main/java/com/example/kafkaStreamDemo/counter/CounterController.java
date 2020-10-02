package com.example.kafkaStreamDemo.counter;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

import static com.example.kafkaStreamDemo.counter.KStreamBindings.WORD_COUNT_ST;

/**
 * @author: piritz
 */
@RestController
@RequestMapping("/counter")
public class CounterController {

	private InteractiveQueryService queryService;

	@Autowired
	public CounterController(InteractiveQueryService queryService) {
		this.queryService = queryService;
	}

	@GetMapping
	public Map<String, Long> counts(){
		Map<String, Long> counts = new HashMap<>();

		ReadOnlyKeyValueStore<Object, Object> queryableStore = queryService.
				getQueryableStore(WORD_COUNT_ST, QueryableStoreTypes.keyValueStore());

		KeyValueIterator<Object, Object> all = queryableStore.all();
		while(all.hasNext()){
			KeyValue<Object, Object> keyValue = all.next();
			counts.put(String.valueOf(keyValue.key), Long.valueOf(keyValue.value.toString()));
		}
		return counts;
	}

}
