package com.zhengsr.lanserver.server;

import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhengshaorui on 2018/7/12.
 */

public class LanServerBean {
    private static Builder mBuilder;


    private LanServerBean(Builder builder){
        mBuilder = builder;
    }

    public static Builder lanBuilder(){
        return new Builder();
    }

    public Builder getBuilder(){
        return mBuilder;
    }

    public static class Builder{
        private int port;
        private String filterString;
        private String htmlString;
        private LinkedHashMap<String, HttpServerRequestCallback> handlerMap = new LinkedHashMap<>();


        public Builder setPort(int port) {
            this.port = port;
            return this;
        }
        public Builder setFilterString(String filterString) {
            this.filterString = filterString;
            return this;
        }
        public Builder setDefaultHtml(String htmlString) {
            this.htmlString = htmlString;
            return this;
        }
        public Builder registerHandler(String name, HttpServerRequestCallback handler) {
            handlerMap.put(name, handler);
            return this;
        }


        public LanServerBean builder(){
            return new LanServerBean(this);
        }
        public int getPort() {
            return port;
        }

        public String getFilterString() {
            return filterString;
        }

        public String getHtmlString() {
            return htmlString;
        }

        public Map<String, HttpServerRequestCallback> getHandlerMap() {
            return handlerMap;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "port=" + port +
                    ", filterString='" + filterString + '\'' +
                    ", htmlString='" + htmlString + '\'' +
                    ", handlerMap=" + handlerMap +
                    '}';
        }
    }
}
