package net.devh.boot.grpc.examples.local.client;

import io.grpc.*;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicefilterConfig {

    static final Metadata.Key<String> X_SERVICEFILTER_SERVICE_NAME =
            Metadata.Key.of("x-servicefilter-target-service-name", Metadata.ASCII_STRING_MARSHALLER);

    @Bean
    GrpcChannelConfigurer servicefilterConfigurer() {
        return (builder, name) -> builder.intercept(new ClientInterceptor() {
            @Override
            public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
                return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {

                    @Override
                    public void start(Listener<RespT> responseListener, Metadata headers) {
                        headers.put(X_SERVICEFILTER_SERVICE_NAME, name);
                        super.start(responseListener, headers);
                    }
                };
            }
        });
    }

}
