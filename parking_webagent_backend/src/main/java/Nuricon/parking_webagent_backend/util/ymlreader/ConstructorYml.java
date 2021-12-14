package Nuricon.parking_webagent_backend.util.ymlreader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("external")
public final class ConstructorYml {
    private final Mqtt mqtt;

    @Getter
    @RequiredArgsConstructor
    public static final class Mqtt {
        private final List<String> urls;
    }
}
