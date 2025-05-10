package org.example.apiconveror.model;

import java.util.Map;

public record DivisaExchangeRate(String base_code, Map<String, Double> conversion_rates) {

}
