package com.solace.maas.ep.event.management.agent.plugin.solace.classifier;

import com.solace.maas.ep.event.management.agent.plugin.solace.classifier.topicTree.TopicTree;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TopicTreeService {

    // The key is the messagingServiceId
    private final Map<String, TopicTree> topicTreeMap = new HashMap<>();

    public void initForScan(String scanId, String topicLevelSeparatorString) {
        if (!topicTreeMap.containsKey(scanId)) {
            topicTreeMap.put(scanId, new TopicTree(topicLevelSeparatorString));
        }
    }

    public void cleanupAfterScan(String messagingServiceId) {
        if (topicTreeMap.containsKey(messagingServiceId)) {
            topicTreeMap.remove(messagingServiceId);
        }
    }

    public TopicTree getTopicTreeForMessagingService(String messagingServiceId) {
        if (topicTreeMap.containsKey(messagingServiceId)) {
            return topicTreeMap.get(messagingServiceId);
        }

        throw new RuntimeException("Could not find topic tree for scanId " + messagingServiceId);
    }
}
