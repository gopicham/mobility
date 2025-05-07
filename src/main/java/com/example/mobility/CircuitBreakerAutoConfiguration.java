package com.example.mobility;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;

@Configuration
public class CircuitBreakerAutoConfiguration {

	@Bean
	public CircuitBreakerRegistry defaultGenesisCircuitBreaker(
			@Value("${application.circuit-breaker.genesis.default."
					+ "ringBufferSizeInClosedState:30}") int ringBufferSizeInClosedState,
			@Value("${application.circuit-breaker.genesis.default."
					+ "ringBufferSizeInHalfOpenState:10}") int ringBufferSizeInHalfOpenState,
			@Value("${application.circuit-breaker.genesis.default."
					+ "failureRateThreshold:50}") long failureRateThreshold,
			@Value("${application.circuit-breaker.genesis.default."
					+ "waitDurationInOpenState:10000}") long waitDurationInOpenState) {

		return CircuitBreakerRegistry.of(circuitBreakerConfig());
	}

	@Bean
	public CircuitBreakerConfig circuitBreakerConfig() {
		return CircuitBreakerConfig.custom().minimumNumberOfCalls(2).failureRateThreshold(50)
				.waitDurationInOpenState(Duration.ofMillis(1000)).permittedNumberOfCallsInHalfOpenState(2)
				.slidingWindowSize(2).build();
	}

	@Bean
	public CircuitBreaker defaultCircuitBreaker() {
		return CircuitBreaker.ofDefaults("default");
	}

	@Bean
	public CircuitBreakerConfigCustomizer testCustomizer() {
		return CircuitBreakerConfigCustomizer.of("default", builder -> builder.slidingWindowSize(100));
	}
}