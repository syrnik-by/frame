package ru.autotestframework.http_steps.components.restassured;

import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.attachment.http.HttpRequestAttachment.Builder.create;

import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import io.qameta.allure.attachment.http.HttpRequestAttachment;
import io.qameta.allure.attachment.http.HttpResponseAttachment;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.FilterContext;
import io.restassured.internal.NameAndValue;
import io.restassured.internal.support.Prettifier;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class AllureRestAssuredFilter extends AllureRestAssured {

    private static final String STEP_NAME = "Отправлен HTTP-запрос";

    private String requestTemplatePath = "http-request.ftl";
    private String responseTemplatePath = "http-response.ftl";
    private String requestAttachmentName = "Request";
    private String responseAttachmentName;

    /**
     * adds response body to allure
     * @param requestSpec
     * @param responseSpec
     * @param filterContext
     * @return
     */
    @Override
    public Response filter(
            final FilterableRequestSpecification requestSpec,
            final FilterableResponseSpecification responseSpec,
            final FilterContext filterContext) {
        final var prettifier = new Prettifier();
        final String url = requestSpec.getURI();
        final var requestAttachmentBuilder = create(requestAttachmentName, url)
                .setMethod(requestSpec.getMethod())
                .setHeaders(toMapConverter(requestSpec.getHeaders()))
                .setCookies(toMapConverter(requestSpec.getCookies()));

        if (Objects.nonNull(requestSpec.getBody())) {
            requestAttachmentBuilder.setBody(prettifier.getPrettifiedBodyIfPossible(requestSpec));
        }

        final StepResult result = new StepResult().setName(STEP_NAME);
        final var uuid = UUID.randomUUID().toString();
        getLifecycle().startStep(uuid, result);

        final HttpRequestAttachment requestAttachment = requestAttachmentBuilder.build();

        new DefaultAttachmentProcessor()
                .addAttachment(requestAttachment, new FreemarkerAttachmentRenderer(requestTemplatePath));

        final var response = filterContext.next(requestSpec, responseSpec);
        if (Objects.isNull(responseAttachmentName)) {
            responseAttachmentName = response.getStatusLine();
        }

        String prettyBody = prettifier.getPrettifiedBodyIfPossible(response, response.getBody());
        String printedBody =
                StringUtils.isBlank(prettyBody) ? response.getBody().asString() : prettyBody;

        final HttpResponseAttachment responseAttachment = HttpResponseAttachment.Builder.create(responseAttachmentName)
                .setResponseCode(response.getStatusCode())
                .setHeaders(toMapConverter(response.getHeaders()))
                .setBody(printedBody)
                .build();

        new DefaultAttachmentProcessor()
                .addAttachment(responseAttachment, new FreemarkerAttachmentRenderer(responseTemplatePath));
        getLifecycle().stopStep();
        return response;
    }

    private static Map<String, String> toMapConverter(final Iterable<? extends NameAndValue> items) {
        final Map<String, String> result = new HashMap<>();
        items.forEach(h -> result.put(h.getName(), h.getValue()));
        return result;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
