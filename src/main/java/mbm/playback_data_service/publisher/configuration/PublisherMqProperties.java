package mbm.playback_data_service.publisher.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "publisher-mq")
public class PublisherMqProperties {
    private String playbackDataTopicName;
}