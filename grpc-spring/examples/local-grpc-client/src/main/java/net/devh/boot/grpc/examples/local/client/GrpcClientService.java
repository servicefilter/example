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

package net.devh.boot.grpc.examples.local.client;

import io.grpc.*;
import net.devh.boot.grpc.examples.lib.RedisGetReply;
import net.devh.boot.grpc.examples.lib.RedisGetRequest;
import org.springframework.stereotype.Service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.examples.lib.HelloReply;
import net.devh.boot.grpc.examples.lib.HelloRequest;
import net.devh.boot.grpc.examples.lib.SimpleGrpc.SimpleBlockingStub;

/**
 * @author Michael (yidongnan@gmail.com)
 * @since 2016/11/8
 */
@Service
public class GrpcClientService {

    @GrpcClient(value = "local-grpc-server")
    private SimpleBlockingStub simpleStub;

    public String sendMessage(final String name) {
        try {
            final HelloReply response = this.simpleStub.sayHello(HelloRequest.newBuilder().setName(name).build());
            return response.getMessage();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
    }

    public String redisGet(final String key) {
        try {
            final RedisGetReply response = this.simpleStub.redisGet(RedisGetRequest.newBuilder().setKey(key).build());
            return response.getValue();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }
    }
}
