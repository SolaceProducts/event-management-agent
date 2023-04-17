package com.solace.maas.ep.event.management.agent.plugin.util;

import com.solace.maas.ep.event.management.agent.plugin.exception.URIException;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class UriUtil {
    public static URI getURI(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            log.error("URI error for {}", url, e);
            throw new URIException(String.format("Could not construct URI from %s", url), e);
        }
        return uri;
    }
}
