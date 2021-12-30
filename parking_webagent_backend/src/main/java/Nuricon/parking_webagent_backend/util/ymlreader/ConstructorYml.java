package Nuricon.parking_webagent_backend.util.ymlreader;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("external")
public final class ConstructorYml {
    private final Mqtt mqtt;
    private final DataManager dataManager;

    @Getter
    @RequiredArgsConstructor
    public static final class Mqtt {
        private final boolean enable;
        private final List<String> urls;
    }

    @Getter
    @RequiredArgsConstructor
    public static final class DataManager {
        private final boolean enable;
        private final String url;
        private final String uri;
    }
}
