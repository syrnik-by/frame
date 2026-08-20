package ru.autotestframework.document_steps;

import static ru.autotestframework.document_steps.json_service.JsonComparator.compare;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("@DocumentSteps")
public class JsonComapratorTest {

    @Test
    void jsonComparePositiveTest() throws IOException {
        String json = "{\n" + "  \"messageType\": [\n"
                + "    \"urn:message:Retail.Loans.LoanPipeline.Api.Contracts.RabbitMq.Events:AppSubmitted\"\n"
                + "  ],\n"
                + "  \"message\": {\n"
                + "    \"AppPath\": \"Ekl\",\n"
                + "    \"RetailAppId\": \"4025429\",\n"
                + "    \"LodgementDate\": \"2026-01-21T00:00:00\",\n"
                + "    \"Period\": 64,\n"
                + "    \"Sum\": 1500000.0,\n"
                + "    \"BrandId\": null,\n"
                + "    \"SubmitterChannelType\": \"Manager\",\n"
                + "    \"BranchCode\": 0,\n"
                + "    \"OfficeCode\": 24,\n"
                + "    \"BranchId\": 1,\n"
                + "    \"OfficeId\": 27,\n"
                + "    \"RegionalOfficeId\": \"neloh\"\n"
                + "  }\n"
                + "}";
        String json2 = "{\n" + "  \"messageType\": [\n"
                + "    \"urn:message:Retail.Loans.LoanPipeline.Api.Contracts.RabbitMq.Events:AppSubmitted\"\n"
                + "  ],\n"
                + "  \"message\": {\n"
                + "    \"AppPath\": \"Ekl\",\n"
                + "    \"RetailAppId\": \"4025429\",\n"
                + "    \"LodgementDate\": \"2026-01-21T00:00:00\",\n"
                + "    \"Period\": 64,\n"
                + "    \"Sum\": 1500000.0,\n"
                + "    \"BrandId\": null,\n"
                + "    \"SubmitterChannelType\": \"Manager\",\n"
                + "    \"BranchCode\": 0,\n"
                + "    \"OfficeCode\": 24,\n"
                + "    \"BranchId\": 5,\n"
                + "    \"OfficeId\": 27,\n"
                + "    \"RegionalOfficeId\": \"loh\"\n"
                + "  }\n"
                + "}";
        JsonNode compare = compare(json, json2);
        Assertions.assertEquals(
                "[{\"op\":\"replace\",\"path\":\"/message/BranchId\",\"value\":5},{\"op\":\"replace\",\"path\":\"/message/RegionalOfficeId\",\"value\":\"loh\"}]",
                compare.toString());
        Assertions.assertEquals(2, compare.size());
    }

    @Test
    void jsonCompareNegativeTest() throws IOException {
        String json = "{\n" + "  \"messageType\": [\n"
                + "    \"urn:message:Retail.Loans.LoanPipeline.Api.Contracts.RabbitMq.Events:AppSubmitted\"\n"
                + "  ],\n"
                + "  \"message\": {\n"
                + "    \"AppPath\": \"Ekl\",\n"
                + "    \"RetailAppId\": \"4025429\",\n"
                + "    \"BranchId\": 1,\n"
                + "    \"OfficeId\": 27\n"
                + "  }\n"
                + "}";
        String json2 = json;
        JsonNode compare = compare(json, json2);
        Assertions.assertEquals("[]", compare.toString());
        Assertions.assertEquals(0, compare.size());
    }
}
