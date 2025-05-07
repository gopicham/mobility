package com.example.mobility;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
public class CircuitBreakerHealthIndicator implements HealthIndicator {

	private final CircuitBreakerRegistry circuitBreakerRegistry;

	public CircuitBreakerHealthIndicator(CircuitBreakerRegistry circuitBreakerRegistry) {
		this.circuitBreakerRegistry = circuitBreakerRegistry;
	}

	@Override
	public Health health() {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("default");
		State state = circuitBreaker.getState();
		
		// Check the current state of the circuit breaker
		if (state == CircuitBreaker.State.OPEN) {
			return Health.down().withDetail("CircuitBreaker", "OPEN").build();
		} else {
			return Health.up().withDetail("CircuitBreaker", "CLOSED").build();
		}
	}
}
