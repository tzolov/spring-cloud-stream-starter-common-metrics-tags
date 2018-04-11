package org.springframework.cloud.stream.app.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Christian Tzolov
 */
@Configuration
public class SpringCloudStreamMicrometerCommonTags {

	@Value("${spring.cloud.dataflow.cluster:default}")
	private String clusterId;

	@Value("${spring.cloud.dataflow.stream.name:unknown}")
	private String streamName;

	@Value("${spring.cloud.dataflow.stream.app.label:unknown}")
	private String applicationName;

	@Value("${instance.index:unknown}")
	private String instanceIndex;

	@Value("${spring.cloud.application.guid:unknown}")
	private String applicationGuid;

	@Value("${spring.cloud.dataflow.stream.app.type:unknown}")
	private String applicationType;

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
		return registry -> registry.config()
				.commonTags("scdf.cluster", clusterId)
				.commonTags("stream", streamName)
				.commonTags("applicationName", applicationName)
				.commonTags("applicationType", applicationType)
				.commonTags("instanceIndex", instanceIndex)
				.commonTags("applicationGuid", applicationGuid);
	}

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> renameNameTag() {
		return registry -> {
			if (registry.getClass().getCanonicalName().contains("AtlasMeterRegistry")) {
				registry.config().meterFilter(MeterFilter.renameTag("spring.integration", "name", "aname"));
			}
		};
	}
}
