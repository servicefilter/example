/*
 * Copyright (c) 2016-2023 The gRPC-Spring Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.devh.boot.grpc.examples.local.server;

import io.github.seal90.servicefilter.sdk.redis.GetRequest;
import io.github.seal90.servicefilter.sdk.redis.GetResponse;
import io.github.seal90.servicefilter.sdk.redis.RedisOperateClient;
import io.github.seal90.servicefilter.sdk.redis.RedisOperateServiceGrpc;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.examples.lib.*;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * @author Michael (yidongnan@gmail.com)
 * @since 2016/11/8
 */

@GrpcService
public class GrpcServerService extends SimpleGrpc.SimpleImplBase {

    static final Metadata.Key<String> X_SERVICEFILTER_SERVICE_NAME =
            Metadata.Key.of("x-servicefilter-target-service-name", Metadata.ASCII_STRING_MARSHALLER);

    @GrpcClient(value = "redis-servicefilter-proxy", interceptors = ServicefilterMetadataClientInterceptor.class)
    private RedisOperateServiceGrpc.RedisOperateServiceBlockingStub redisOperateServiceBlockingStub;

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello ==> " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void redisGet(RedisGetRequest req, StreamObserver<RedisGetReply> responseObserver) {
        GetRequest getRequest = GetRequest.newBuilder().setKey(req.getKey()).build();
        GetResponse response = redisOperateServiceBlockingStub.get(getRequest);
        RedisGetReply reply = RedisGetReply.newBuilder().setValue("redisGet ==> " + response.getValue()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    public static class ServicefilterMetadataClientInterceptor implements ClientInterceptor {
        @Override
        public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {

                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    headers.put(X_SERVICEFILTER_SERVICE_NAME, "redis-servicefilter-proxy");
                    super.start(responseListener, headers);
                }
            };
        }
    }

}
