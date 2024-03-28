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

package net.devh.boot.grpc.examples.cloud.server;

import org.springframework.context.annotation.Configuration;

import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

/**
 * Example configuration class that adds a {@link ServerInterceptor} to the global interceptor list.
 */
@Configuration(proxyBeanMethods = false)
public class GlobalInterceptorConfiguration {

    /**
     * Creates a new {@link LogGrpcInterceptor} bean and adds it to the global interceptor list. As an alternative you
     * can directly annotate the {@code LogGrpcInterceptor} class and it will automatically be picked up by spring's
     * classpath scanning.
     *
     * @return The newly created bean.
     */
    @GrpcGlobalServerInterceptor
    LogGrpcInterceptor logServerInterceptor() {
        return new LogGrpcInterceptor();
    }

}
