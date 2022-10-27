package com.solace.maas.ep.event.management.agent.plugin.solace.processor.semp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Getter
public class SolaceHttpSemp {
    private final static String GET_SYSTEM_INFORMATION = "/SEMP/v2/config/about/api";
    private final static String GET_QUEUES_URI = "/SEMP/v2/config/msgVpns/{msgvpn}/queues";
    private final static String GET_TOPIC_SUBSCRIPTIONS_FOR_QUEUE_URI = "/SEMP/v2/config/msgVpns/{msgvpn}/queues/{queuename}/subscriptions";
    private final ObjectMapper objectMapper;
    private final SempClient sempClient;
    private final int SEMP_PAGE_SIZE = 100;

    public SolaceHttpSemp(SempClient sempClient) {
        this.sempClient = sempClient;
        objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getSempVersion() {
        Map<String, Object> rawSystemInformation = getResultsFlatFromSemp(GET_SYSTEM_INFORMATION);
        String sempVersion = (String) rawSystemInformation.get("sempVersion");
        log.debug("Using semp version {}", sempVersion);
        return sempVersion;
    }

    public List<Map<String, Object>> getQueueNames() {
        return getResultsListMapFromSemp(GET_QUEUES_URI, null, "queueName");
    }

    public List<Map<String, Object>> getQueues() {
        return getResultsListMapFromSemp(GET_QUEUES_URI);
    }

    public List<Map<String, Object>> getSubscriptionForQueue(String queueName) {
        return getResultsListMapFromSemp(
                GET_TOPIC_SUBSCRIPTIONS_FOR_QUEUE_URI,
                Map.of("queuename", queueName),
                null);
    }

    private List<Map<String, Object>> getResultsListMapFromSemp(String uriPath) {
        return getResultsListMapFromSemp(uriPath, null, null);
    }

    private Map<String, Object> getResultsFlatFromSemp(String uriPath) {
        try {
            return getSempFlatRequest(createFlatUriBuilderFunction(uriPath, Collections.emptyMap()));
        } catch (IOException ioException) {
            log.error("Error during SEMP Data Collection", ioException);
            throw new SempException(ioException);
        }

    }

    private List<Map<String, Object>> getResultsListMapFromSemp(String uriPath,
                                                                Map<String, String> additionalSubstitutionFields,
                                                                String selectFields) {
        List<Map<String, Object>> sempObject = new ArrayList<>();
        Map<String, String> substitutionMap = new HashMap<>();
        substitutionMap.put("msgvpn", sempClient.getMsgVpn());
        if (!MapUtils.isEmpty(additionalSubstitutionFields)) {
            substitutionMap.putAll(additionalSubstitutionFields);
        }

        try {
            getSempListRequest(sempObject, createUriBuilderFunction(uriPath, substitutionMap, selectFields));
        } catch (WebClientResponseException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST:
                    log.error("Error during SEMP Data Collection. Invalid path to data." +
                            " Check that the SEMP URL and protocol are correct.", ex);
                    throw new SempException(ex);
                case UNAUTHORIZED:
                    log.error("Error during SEMP Data Collection. Could not authenticate with the server." +
                            "Check that the SEMP username and password are correct.", ex);
                    throw new SempException(ex);
                default:
                    log.error("Error during SEMP Data Collection.", ex);
                    throw new SempException(ex);
            }
        } catch (IOException ioException) {
            log.error("Error during SEMP Data Collection. The format of the collected data is unexpected.", ioException);
            throw new SempException(ioException);
        } catch (WebClientRequestException requestException) {
            if (requestException.getMessage().startsWith("Failed to resolve")) {
                log.error("Error connecting to messaging service. Check that the hostname is correct.", requestException);
                throw new SempException(requestException);
            }
            log.error("Error connecting to messaging service. Check that the port is correct", requestException);
            throw new SempException(requestException);
        }
        return sempObject;
    }

    private Function<UriBuilder, URI> createUriBuilderFunction(String uriPath,
                                                               Map<String, String> substitutionMap,
                                                               String selectFields) {
        URI sempUri = getSempUri();

        if (StringUtils.isNotEmpty(selectFields)) {
            return (uriBuilder) -> uriBuilder
                    .path(uriPath)
                    .queryParam("select", selectFields)
                    .host(sempUri.getHost())
                    .port(sempUri.getPort())
                    .scheme(sempUri.getScheme())
                    .queryParam("count", SEMP_PAGE_SIZE)
                    .build(substitutionMap);
        }

        return (uriBuilder) -> uriBuilder
                .path(uriPath)
                .host(sempUri.getHost())
                .port(sempUri.getPort())
                .scheme(sempUri.getScheme())
                .queryParam("count", SEMP_PAGE_SIZE)
                .build(substitutionMap);
    }

    private Function<UriBuilder, URI> createFlatUriBuilderFunction(String uriPath,
                                                                   Map<String, String> substitutionMap) {
        URI sempUri = getSempUri();
        return (uriBuilder) -> uriBuilder
                .path(uriPath)
                .host(sempUri.getHost())
                .port(sempUri.getPort())
                .scheme(sempUri.getScheme())
                .build(substitutionMap);
    }

    private URI getSempUri() {
        URI sempUri = null;
        try {
            sempUri = new URI(sempClient.getConnectionUrl());
        } catch (URISyntaxException e) {
            log.error("URI error for {}", sempClient.getConnectionUrl(), e);
            throw new RuntimeException(String.format("Could not construct URL from %s", sempClient.getConnectionUrl()), e);
        }
        return sempUri;
    }

    private void getSempListRequest(List<Map<String, Object>> list, Function<UriBuilder, URI> uriMethod) throws
            IOException {
        SempListResponse<Map<String, Object>> sempListResponse = getSempListResponse(uriMethod);

        if (sempListResponse != null) {
            if (!CollectionUtils.isEmpty(sempListResponse.getData())) {
                list.addAll(sempListResponse.getData());
            }

            handlePagedSempResponse(list, sempListResponse);
        }
    }

    private void handlePagedSempResponse(List<Map<String, Object>> list,
                                         SempListResponse<Map<String, Object>> sempListResponse) throws com.fasterxml.jackson.core.JsonProcessingException {
        Optional<Paging> paging = Optional.ofNullable(sempListResponse.getMeta().getPaging());
        while (paging.isPresent()) {
            log.debug("Paging {}", paging.get().getNextPageUri());
            final Paging pagingCopy = paging.get();
            sempListResponse = getSempListResponse((uriBuilder) -> URI.create(pagingCopy.getNextPageUri()));
            if (sempListResponse != null) {
                if (!CollectionUtils.isEmpty(sempListResponse.getData())) {
                    list.addAll(sempListResponse.getData());
                }
                paging = Optional.ofNullable(sempListResponse.getMeta().getPaging());
            } else {
                paging = Optional.empty();
            }
        }
    }

    private SempListResponse<Map<String, Object>> getSempListResponse(Function<UriBuilder, URI> uriMethod) throws
            com.fasterxml.jackson.core.JsonProcessingException {
        String rawResponse = sempClient.getWebClient()
                .get()
                .uri(uriMethod)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorization())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return objectMapper.readValue(rawResponse,
                new TypeReference<>() {
                });
    }

    private Map<String, Object> getSempFlatRequest(Function<UriBuilder, URI> uriMethod) throws IOException {
        String rawResponse = sempClient.getWebClient()
                .get()
                .uri(uriMethod)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, buildBasicAuthorization())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        SempFlatResponse<Map<String, Object>> sempFlatResponse = objectMapper.readValue(rawResponse,
                new TypeReference<>() {
                });

        if (sempFlatResponse != null) {
            return sempFlatResponse.getData();
        }
        return null;
    }

    private String buildBasicAuthorization() {
        return "Basic " +
                Base64.getEncoder()
                        .encodeToString((sempClient.getUsername() + ':' + sempClient.getPassword())
                                .getBytes(StandardCharsets.UTF_8));
    }
}
