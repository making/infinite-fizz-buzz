package com.example;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.Duration;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

@SpringBootApplication
public class InfiniteFizzBuzzApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfiniteFizzBuzzApplication.class, args);
	}

	// RouterFunctions

	@Bean
	RouterFunction<ServerResponse> routes() {
		return RouterFunctions
				.route(GET("/fizzbuzz"),
						req -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
								.body(fizzbuzz(Duration.ofMillis(100)), String.class))
				.and(staticResources());
	}

	static RouterFunction<ServerResponse> staticResources() {
		return RouterFunctions.resources("/**", new ClassPathResource("static/"));
	}

	// FizzBuzz

	static Flux<String> fizzbuzz(Duration duration) {
		return Flux.fromStream(fizzbuzz()).zipWith(Flux.interval(duration))
				.map(Tuple2::getT1);
	}

	static Stream<String> fizzbuzz() {
		return IntStream.iterate(1, i -> i + 1).mapToObj(x -> fizzbuzz(x));
	}

	static String fizzbuzz(int x) {
		if (x % 15 == 0) {
			return "FizzBuzz";
		}
		if (x % 5 == 0) {
			return "Buzz";
		}
		if (x % 3 == 0) {
			return "Fizz";
		}
		return String.valueOf(x);
	}
}
