package cn.sliew.http.stream.remote.config;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.okhttp.OkHttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableFeignClients(basePackages = "cn.sliew.http.stream.remote")
public class DefaultFeignConfig {

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonConverter;

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public Encoder encoder() {
        addAdditionalMediaType();
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new SpringEncoder(objectFactory);
    }

    @Bean
    public Decoder decoder() {
        addAdditionalMediaType();
        HttpMessageConverters httpMessageConverters = new HttpMessageConverters(jacksonConverter);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> httpMessageConverters;
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    private void addAdditionalMediaType() {
        List<MediaType> supportedMediaTypes = new LinkedList<>(jacksonConverter.getSupportedMediaTypes());
        supportedMediaTypes.addAll(Arrays.asList(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN));
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
    }
}
