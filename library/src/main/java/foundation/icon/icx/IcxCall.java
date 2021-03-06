/*
 * Copyright 2018 ICON Foundation
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
 *
 */

package foundation.icon.icx;

import foundation.icon.icx.data.Address;
import foundation.icon.icx.transport.jsonrpc.RpcItem;
import foundation.icon.icx.transport.jsonrpc.RpcItemCreator;
import foundation.icon.icx.transport.jsonrpc.RpcObject;
import foundation.icon.icx.transport.jsonrpc.RpcValue;

/**
 * IcxCall contains parameters for querying request.
 *
 * @param <O> Response type
 */
public final class IcxCall<O> {

    private RpcObject properties;
    private Class<O> responseType;

    private IcxCall(RpcObject properties, Class<O> responseType) {
        this.properties = properties;
        this.responseType = responseType;
    }


    RpcObject getProperties() {
        return properties;
    }

    Class<O> responseType() {
        return responseType;
    }

    /**
     * Builder for creating immutable object of IcxCall.<br />
     * It has following properties<br />
     * - {@link #from(String)} the request account<br />
     * - {@link #to(String)} the SCORE address to call<br />
     * - {@link #method(String)} )} the method name to call<br />
     * - {@link #params(Object)}  the parameter of call<br />
     */
    @SuppressWarnings("WeakerAccess")
    public static class Builder {
        private Address from;
        private Address to;
        private String method;
        private RpcItem params;

        public Builder() {
        }

        public Builder from(Address from) {
            this.from = from;
            return this;
        }

        public Builder to(Address to) {
            this.to = to;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public <I> Builder params(I params) {
            this.params = RpcItemCreator.create(params);
            return this;
        }

        public Builder params(RpcItem params) {
            this.params = params;
            return this;
        }

        /**
         * Builds with RpcItem. that means the return type is RpcItem
         *
         * @return IcxCall
         */
        public IcxCall<RpcItem> build() {
            return buildWith(RpcItem.class);
        }

        /**
         * Builds with User defined class. an object of the class would be returned
         *
         * @param responseType Response type
         * @return IcxCall
         */
        public <O> IcxCall<O> buildWith(Class<O> responseType) {
            RpcObject data = new RpcObject.Builder()
                    .put("method", new RpcValue(method))
                    .put("params", params)
                    .build();

            RpcObject properties = new RpcObject.Builder()
                    .put("from", new RpcValue(from))
                    .put("to", new RpcValue(to))
                    .put("data", data)
                    .put("dataType", new RpcValue("call"))
                    .build();
            return new IcxCall<>(properties, responseType);
        }
    }

}
