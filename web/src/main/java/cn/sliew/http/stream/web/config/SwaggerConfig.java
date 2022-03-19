package cn.sliew.http.stream.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.function.Predicate;

@Slf4j
@Configuration
public class SwaggerConfig {

    private static final String ERROR_HANDLER_PATH = "/error/**";
    private static final String ACTUATOR_HANDLER_PATH = "/actuator/**";

    @Bean
    public Docket docket() {
        Predicate<String> error = PathSelectors.ant(ERROR_HANDLER_PATH);
        Predicate<String> actuator = PathSelectors.ant(ACTUATOR_HANDLER_PATH);
        Predicate<String> or = error.or(actuator);
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select().paths(or.negate())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("demo for http api")
                .description("demo for http！有事联系wangqi~")
                .version("1.0.0")
                .contact(new Contact("wangqi", "http://www.sliew.cn", "wangqi@sliew.cn"))
                .build();
    }

}