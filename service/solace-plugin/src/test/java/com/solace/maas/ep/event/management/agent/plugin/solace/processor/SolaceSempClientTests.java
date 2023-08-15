package com.solace.maas.ep.event.management.agent.plugin.solace.processor;

import com.solace.maas.ep.event.management.agent.plugin.solace.SolaceTestConfig;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempClient;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SempException;
import com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp.SolaceHttpSemp;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("TEST")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SolaceTestConfig.class)
@SuppressWarnings("PMD")
public class SolaceSempClientTests {
    private final DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();

    private final Map<String, String> requestResponseMap = buildRequestResponseMap();

    @Captor
    private ArgumentCaptor<Function<UriBuilder, URI>> uriFunctionCaptor;

    @Mock
    private WebClient mockClient;

    private SolaceHttpSemp sempClient;

    @BeforeEach
    public void setupMocks() {
        SempClient baseClient = SempClient.builder()
                .connectionUrl("http://myHost:12345")
                .webClient(mockClient)
                .username("myUsername")
                .password("myPassword")
                .msgVpn("xyz")
                .build();

        mockHttp(mockClient);

        sempClient = new SolaceHttpSemp(baseClient);

    }

    @SneakyThrows
    @Test
    public void getSempVersion() {
        String version = sempClient.getSempVersion();
        assertThat("Semp version", version.equals("2.7"));
        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void getQueueNamesTest() {
        List<Map<String, Object>> queues = sempClient.getQueueNames();

        assertThat(queues, hasSize(2));

        List<String> queueNames = queues.stream().map(qMap -> qMap.get("queueName").toString()).collect(Collectors.toUnmodifiableList());
        assertThat(queueNames, containsInAnyOrder("testQueue", "testQueue2"));
        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void getQueueConfigTest() {
        List<Map<String, Object>> queues = sempClient.getQueues();

        assertThat(queues, hasSize(2));

        List<String> accessTypes = queues.stream().map(qMap -> qMap.get("accessType").toString()).collect(Collectors.toUnmodifiableList());
        assertThat(accessTypes, containsInAnyOrder("non-exclusive", "exclusive"));
        assertThatNoException();
    }

    @SneakyThrows
    @Test
    public void getSubscriptionsTest() {
        List<Map<String, Object>> subscriptionList = sempClient.getSubscriptionForQueue("myQueue1");

        assertThat(subscriptionList, hasSize(2));

        List<String> subscriptions = subscriptionList.stream()
                .map(subMap -> subMap.get("subscriptionTopic").toString()).collect(Collectors.toUnmodifiableList());
        assertThat(subscriptions, containsInAnyOrder("my/fancy/topic/subscription", "my/fancy/topic/subscription2"));
        assertThatNoException();
    }

    @Test
    public void badConnectionUrlTest() throws IOException {

        // Create a bad connection url
        SempClient baseClient = SempClient.builder()
                .connectionUrl("worst url ever")
                .webClient(mockClient)
                .username("myUsername")
                .password("myPassword")
                .msgVpn("xyz")
                .build();
        sempClient = new SolaceHttpSemp(baseClient);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> sempClient.getSubscriptionForQueue("myQueue1")
        );

        assertEquals("Could not construct URI from worst url ever", exception.getMessage());
    }

    @Test
    public void throwBadRequestExceptionWhileGettingQueues() {
        WebClient mockWebClient = mock(WebClient.class);

        when(mockWebClient.get()).thenThrow(new WebClientResponseException(HttpStatus.BAD_REQUEST.value(), "bad", null, null, null));

        SempClient mockSempClient = SempClient.builder()
                .connectionUrl("http://myHost:12345")
                .webClient(mockWebClient)
                .username("myUsername")
                .password("myPassword")
                .msgVpn("xyz")
                .build();
        SolaceHttpSemp semp = new SolaceHttpSemp(mockSempClient);
        SempException ex = Assertions.assertThrows(SempException.class, () -> {
            semp.getQueues();
        });
        assertEquals(400, ((WebClientResponseException) ex.getCause()).getStatusCode().value());
    }

    @Test
    public void throwUnauthorizedExceptionWhileGettingQueues() {
        WebClient mockWebClient = mock(WebClient.class);

        when(mockWebClient.get()).thenThrow(new WebClientResponseException(HttpStatus.UNAUTHORIZED.value(), "bad", null, null, null));

        SempClient mockSempClient = SempClient.builder()
                .connectionUrl("http://myHost:12345")
                .webClient(mockWebClient)
                .username("myUsername")
                .password("myPassword")
                .msgVpn("xyz")
                .build();
        SolaceHttpSemp semp = new SolaceHttpSemp(mockSempClient);
        SempException ex = Assertions.assertThrows(SempException.class, () -> {
            semp.getQueues();
        });
        assertEquals(401, ((WebClientResponseException) ex.getCause()).getStatusCode().value());
    }

    private void mockHttp(WebClient webClient) {
        WebClient.RequestHeadersUriSpec request = mock(WebClient.RequestHeadersUriSpec.class);
        when(webClient.get()).thenReturn(request);

        when(request.uri(uriFunctionCaptor.capture())).thenAnswer(
                (Answer) invocation -> {
                    URI newUri = uriFunctionCaptor.getValue().apply(defaultUriBuilderFactory.builder());
                    return createWebClientResponse(requestResponseMap.get(newUri.toString()));
                }
        );
    }

    private WebClient.RequestHeadersSpec createWebClientResponse(String response) {
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.accept(any(MediaType.class))).thenReturn(requestHeadersSpec);

        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        Mono monoSubjectList = mock(Mono.class);

        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(monoSubjectList);
        when(monoSubjectList.block()).thenReturn(response);

        return requestHeadersSpec;
    }

    private Map<String, String> buildRequestResponseMap() {
        Map<String, String> requestResponseResult = new HashMap<>();

        // Add response for get queue names page 1
        requestResponseResult.put("http://myHost:12345/SEMP/v2/config/msgVpns/xyz/queues?select=queueName&count=100",
                "{ \"data\": [" +
                        "        {\"queueName\": \"testQueue\"}" +
                        "]," +
                        "    \"meta\": {\n" +
                        "        \"paging\": {\n" +
                        "             \"nextPageUri\": \"http://myHost:12345/secondPage\"" +
                        "        },\n" +
                        "        \"request\": {\n" +
                        "            \"method\": \"GET\",\n" +
                        "            \"uri\": \"http://myHost:12345/SEMP/v2/config/msgVpns/xyz/queues?count=100\"\n" +
                        "        },\n" +
                        "        \"responseCode\": 200\n" +
                        "    } }"
        );

        // Add response for get queue names page 2
        requestResponseResult.put("http://myHost:12345/secondPage",
                "{ \"data\": [" +
                        "        {\"queueName\": \"testQueue2\"}" +
                        "]," +
                        "    \"meta\": {\n" +
                        "        \"request\": {\n" +
                        "            \"method\": \"GET\",\n" +
                        "            \"uri\": \"http://myHost:12345/secondPage\"\n" +
                        "        },\n" +
                        "        \"responseCode\": 200\n" +
                        "    } }"
        );

        // Add response for get queue configuration
        requestResponseResult.put("http://myHost:12345/SEMP/v2/config/msgVpns/xyz/queues?count=100",
                "{\n" +
                        "    \"data\": [\n" +
                        "        {\n" +
                        "            \"accessType\": \"non-exclusive\",\n" +
                        "            \"consumerAckPropagationEnabled\": true,\n" +
                        "            \"deadMsgQueue\": \"#DEAD_MSG_QUEUE\",\n" +
                        "            \"deliveryCountEnabled\": false,\n" +
                        "            \"deliveryDelay\": 0,\n" +
                        "            \"egressEnabled\": true,\n" +
                        "            \"eventBindCountThreshold\": {\n" +
                        "                \"clearPercent\": 60,\n" +
                        "                \"setPercent\": 80\n" +
                        "            },\n" +
                        "            \"eventMsgSpoolUsageThreshold\": {\n" +
                        "                \"clearPercent\": 60,\n" +
                        "                \"setPercent\": 80\n" +
                        "            },\n" +
                        "            \"eventRejectLowPriorityMsgLimitThreshold\": {\n" +
                        "                \"clearPercent\": 60,\n" +
                        "                \"setPercent\": 80\n" +
                        "            },\n" +
                        "            \"ingressEnabled\": true,\n" +
                        "            \"maxBindCount\": 1000,\n" +
                        "            \"maxDeliveredUnackedMsgsPerFlow\": 10000,\n" +
                        "            \"maxMsgSize\": 10000000,\n" +
                        "            \"maxMsgSpoolUsage\": 1500,\n" +
                        "            \"maxRedeliveryCount\": 0,\n" +
                        "            \"maxTtl\": 0,\n" +
                        "            \"msgVpnName\": \"xyz\",\n" +
                        "            \"owner\": \"\",\n" +
                        "            \"permission\": \"consume\",\n" +
                        "            \"queueName\": \"BuyQueue\",\n" +
                        "            \"redeliveryEnabled\": true,\n" +
                        "            \"rejectLowPriorityMsgEnabled\": false,\n" +
                        "            \"rejectLowPriorityMsgLimit\": 0,\n" +
                        "            \"rejectMsgToSenderOnDiscardBehavior\": \"when-queue-enabled\",\n" +
                        "            \"respectMsgPriorityEnabled\": false,\n" +
                        "            \"respectTtlEnabled\": false\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"accessType\": \"exclusive\",\n" +
                        "            \"consumerAckPropagationEnabled\": true,\n" +
                        "            \"deadMsgQueue\": \"#DEAD_MSG_QUEUE\",\n" +
                        "            \"deliveryCountEnabled\": false,\n" +
                        "            \"deliveryDelay\": 0,\n" +
                        "            \"egressEnabled\": true,\n" +
                        "            \"eventBindCountThreshold\": {\n" +
                        "                \"clearPercent\": 60,\n" +
                        "                \"setPercent\": 80\n" +
                        "            },\n" +
                        "            \"eventMsgSpoolUsageThreshold\": {\n" +
                        "                \"clearPercent\": 18,\n" +
                        "                \"setPercent\": 25\n" +
                        "            },\n" +
                        "            \"eventRejectLowPriorityMsgLimitThreshold\": {\n" +
                        "                \"clearPercent\": 60,\n" +
                        "                \"setPercent\": 80\n" +
                        "            },\n" +
                        "            \"ingressEnabled\": true,\n" +
                        "            \"maxBindCount\": 1000,\n" +
                        "            \"maxDeliveredUnackedMsgsPerFlow\": 10000,\n" +
                        "            \"maxMsgSize\": 10000000,\n" +
                        "            \"maxMsgSpoolUsage\": 5000,\n" +
                        "            \"maxRedeliveryCount\": 0,\n" +
                        "            \"maxTtl\": 0,\n" +
                        "            \"msgVpnName\": \"xyz\",\n" +
                        "            \"owner\": \"\",\n" +
                        "            \"permission\": \"consume\",\n" +
                        "            \"queueName\": \"DelayQueue\",\n" +
                        "            \"redeliveryEnabled\": true,\n" +
                        "            \"rejectLowPriorityMsgEnabled\": false,\n" +
                        "            \"rejectLowPriorityMsgLimit\": 0,\n" +
                        "            \"rejectMsgToSenderOnDiscardBehavior\": \"when-queue-enabled\",\n" +
                        "            \"respectMsgPriorityEnabled\": false,\n" +
                        "            \"respectTtlEnabled\": false\n" +
                        "        }],\n" +
                        "    \"meta\": {\n" +
                        "        \"request\": {\n" +
                        "            \"method\": \"GET\",\n" +
                        "            \"uri\": \"https://mroyppj81pus7.messaging.solace.cloud:943/SEMP/v2/config/msgVpns/xyz/queues\"\n" +
                        "        },\n" +
                        "        \"responseCode\": 200\n" +
                        "    }\n" +
                        "}"
        );


        // Add response for subscriptions
        requestResponseResult.put("http://myHost:12345/SEMP/v2/config/msgVpns/xyz/queues/myQueue1/subscriptions?count=100",
                "{ \"data\": [" +
                        "        {\"queueName\": \"testQueue1\"," +
                        "         \"msgVpnName\": \"xyz\"," +
                        "         \"subscriptionTopic\": \"my/fancy/topic/subscription\" }," +
                        "        {\"queueName\": \"testQueue1\"," +
                        "         \"msgVpnName\": \"xyz\"," +
                        "         \"subscriptionTopic\": \"my/fancy/topic/subscription2\" }" + "        ]," +
                        "    \"meta\": {\n" +
                        "        \"request\": {\n" +
                        "            \"method\": \"GET\",\n" +
                        "            \"uri\": \"http://myHost:12345/SEMP/v2/config/msgVpns/xyz/queues/myQueue1/subscriptions?count=100\"\n" +
                        "        },\n" +
                        "        \"responseCode\": 200\n" +
                        "    } }"


        );

        //Add semp version
        requestResponseResult.put("http://myHost:12345/SEMP/v2/config/about/api", "{\"data\": {\"sempVersion\": \"2.7\"}}");
        return requestResponseResult;
    }
}
