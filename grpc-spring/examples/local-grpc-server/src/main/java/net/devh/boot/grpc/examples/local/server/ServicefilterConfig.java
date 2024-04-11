package net.devh.boot.grpc.examples.local.server;

import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicefilterConfig {

    static final Metadata.Key<String> X_SERVICEFILTER_SERVICE_NAME =
            Metadata.Key.of("x-servicefilter-target-service-name", Metadata.ASCII_STRING_MARSHALLER);

    @Bean
    GrpcChannelConfigurer servicefilterConfigurer() {
        return (builder, name) -> {
            Metadata extraHeaders = new Metadata();
            extraHeaders.put(X_SERVICEFILTER_SERVICE_NAME,name);
            builder.intercept(MetadataUtils.newAttachHeadersInterceptor(extraHeaders));
        };
    }

}
