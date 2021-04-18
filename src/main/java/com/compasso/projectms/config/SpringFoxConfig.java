package com.compasso.projectms.config;

import com.compasso.projectms.api.resource.exceptions.StandardError;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    final TypeResolver typeResolver = new TypeResolver();

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.compasso"))
                .build()
                .apiInfo(metaData())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, responseGet())
                .globalResponses(HttpMethod.POST, responsePost())
                .globalResponses(HttpMethod.PUT, responsePut())
                .globalResponses(HttpMethod.DELETE, responseDelete())
                .additionalModels(typeResolver.resolve(StandardError.class));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Spring Boot REST API")
                .description("\"Spring Boot REST API\"")
                .version("1.0.0")
                .build();
    }

    private List<Response> responseGet() {

        List<Response> list = new ArrayList<>();

        list.add(new ResponseBuilder()
                .code("404")
                .description("Resource not found")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());

        list.add(new ResponseBuilder()
                .code("500")
                .description("Internal Error")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());
        return list;
    }

    private List<Response> responsePost() {

        List<Response> list = new ArrayList<>();

        list.add(new ResponseBuilder()
                .code("400")
                .description("Bad Request")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());

        return list;
    }

    private List<Response> responsePut() {

        List<Response> list = new ArrayList<>();

        list.add(new ResponseBuilder()
                .code("404")
                .description("Not found")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());

        list.add(new ResponseBuilder()
                .code("400")
                .description("Bad Request")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());

        return list;
    }

    private List<Response> responseDelete() {

        List<Response> list = new ArrayList<>();

        list.add(new ResponseBuilder()
                .code("404")
                .description("Not Found")
                .representation(MediaType.APPLICATION_JSON)
                .apply(r -> r.model
                        (m -> m.referenceModel(
                                ref -> ref.key(
                                        k -> k.qualifiedModelName(
                                                q -> q.namespace("com.compasso.projectms.api.resource.exceptions")
                                                        .name("StandardError"))))))
                .build());

        return list;
    }
}

