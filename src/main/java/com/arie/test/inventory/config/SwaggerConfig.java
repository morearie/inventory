package com.arie.test.inventory.config;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${swagger.host}")
	private String swaggerHost;
	
	@Value("${swagger.path}")
	private String swaggerPort;
    /**
     * Configuration swagger with Authorization header
     * if prefix url needed, set value at ${swagger.path-mapping}
     *
     * @return docker api
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.globalOperationParameters(operationParameters())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .host(swaggerHost)
                .pathMapping(swaggerPort);
//                .securitySchemes(getSecuritySchemes())
//                .securityContexts(getSecurityContexts());
    }

//    private List<ApiKey> getSecuritySchemes() {
//        return Collections.singletonList(new ApiKey("JWT", "Authorization", "header"));
//    }
//
//    private List<SecurityContext> getSecurityContexts() {
//        return Collections.singletonList(
//                SecurityContext.builder().securityReferences(
//                        Collections.singletonList(SecurityReference.builder()
//                                .reference("JWT")
//                                .scopes(new AuthorizationScope[0])
//                                .build()
//                        )
//                ).build()
//        );
//    }
    
    /**
     * Pageable convention.
     *
     * @param resolver the resolver
     * @return the alternate type rule convention
     */
    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
                        new AlternateTypeRule(resolver.resolve(Pageable.class), resolver.resolve(pageableMixin())));
            }
        };
    }

    private Type pageableMixin() {
        return new AlternateTypeBuilder()
                .fullyQualifiedClassName(String.format("%s.generated.%s", Pageable.class.getPackage().getName(),
                        Pageable.class.getSimpleName()))
                .withProperties(Arrays.asList(property(Integer.class, "page"), property(String[].class, "sort"),
                        property(Integer.class, "size"), property(Boolean.class, "unpaged")))
                .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder().withName(name).withType(type).withCanRead(true).withCanWrite(true);
    }
    
    private List<Parameter> operationParameters() {
		List<Parameter> headers = new ArrayList<>();
		headers.add(new ParameterBuilder().name("Accept-Language").description("Accept Language fo i18n").defaultValue("en")
				.modelRef(new ModelRef("string")).parameterType("header").required(false).build());
		return headers;
	}
}
