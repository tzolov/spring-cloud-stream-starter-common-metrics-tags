package org.springframework.cloud.stream.app.micrometer;

import java.util.Map;

import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Christian Tzolov
 */
@Configuration
public class SpringCloudStreamMicrometerCommonTags {

	private Map<String, String> commonTags;


	@Value("${spring.cloud.dataflow.cluster:default}")
	private String clusterId;

	@Value("${spring.cloud.dataflow.stream.name:unknown}")
	private String streamName;

	@Value("${spring.cloud.dataflow.stream.app.label:unknown}")
	private String applicationName;

	@Value("${INSTANCE_INDEX:unknown}")
	private String instanceIndex;

	@Value("${SPRING_CLOUD_APPLICATION_GUID:unknown}")
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
}
