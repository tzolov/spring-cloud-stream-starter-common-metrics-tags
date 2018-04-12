/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.stream.app.micrometer;

import java.util.Arrays;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * By default disables all micrometer monitoring systems.
 * Currently if a micrometer metric registry is found on the class path, later will be enabled automatically. This
 * postprocessor reverts this behavior, requiring an monitoring system to be explicitly enabled first. E.g.
 * Set the management.metrics.export.MonitoringSystem.enabled=true
 *
 * @author Christian Tzolov
 */
public class MicrometerMetricsDisabledByDefaultEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private String[] monitoringSystemNames = new String[] {
			"atlas", "datadog", "ganglia", "graphite", "influx", "newrelic", "prometheus", "signalfx", "simple",
			"statsd", "wavefront"
	};

	protected static final String PROPERTY_SOURCE_KEY_NAME = MicrometerMetricsDisabledByDefaultEnvironmentPostProcessor.class.getName();
	protected static final String METRICS_EXPORT_PREFIX = "management.metrics.export.";
	protected static final String METRICS_EXPORT_SUFFIX = ".enabled";

	public MicrometerMetricsDisabledByDefaultEnvironmentPostProcessor() {
	}

	public MicrometerMetricsDisabledByDefaultEnvironmentPostProcessor(String[] metricsEnabledPropertyKeys) {
		this.monitoringSystemNames = metricsEnabledPropertyKeys;
	}

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment configurableEnvironment, SpringApplication application) {
		Properties properties = new Properties();
		Arrays.stream(monitoringSystemNames)
				.map(monitoringSystem -> METRICS_EXPORT_PREFIX + monitoringSystem + METRICS_EXPORT_SUFFIX)
				.filter(metricsEnabledProperty -> !configurableEnvironment.containsProperty(metricsEnabledProperty))
				.forEach(metricsEnabledProperty -> properties.setProperty(metricsEnabledProperty, "false"));

		if (!properties.isEmpty()) {
			PropertiesPropertySource propertiesPropertySource =
					new PropertiesPropertySource(PROPERTY_SOURCE_KEY_NAME, properties);
			configurableEnvironment.getPropertySources().addLast(propertiesPropertySource);
		}
	}
}
