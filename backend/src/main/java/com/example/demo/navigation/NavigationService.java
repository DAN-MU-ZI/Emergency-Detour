package com.example.demo.navigation;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.utils.VWorldApiUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class NavigationService {

    private final VWorldApiUtils vWorldApiUtils;
    private final ObjectMapper om;

    public ResponseEntity<?> searchPlaces(String location) throws Exception {
        String vWorldResponse = vWorldApiUtils.search(location, "place", null);
        JsonNode parsedResponse = om.readTree(vWorldResponse);

        ObjectNode responseNode = (ObjectNode) om.createObjectNode();

        if (parsedResponse.has("response")) {
            JsonNode responseNodeFromApi = parsedResponse.get("response");

            if (responseNodeFromApi.has("result")) {
                JsonNode resultNode = responseNodeFromApi.get("result");
                JsonNode itemsNode = resultNode.get("items");

                if (itemsNode != null && itemsNode.isArray()) {
                    for (JsonNode itemNode : itemsNode) {
                        ObjectNode responseResultItemNode = om.createObjectNode();
                        responseResultItemNode.put("title", itemNode.get("title").asString());
                        responseResultItemNode.put("address", itemNode.get("address").get("parcel").asString());
                        responseResultItemNode.put("id", itemNode.get("id").asString());
                        responseResultItemNode.put("lat", itemNode.get("point").get("y").asDouble());
                        responseResultItemNode.put("lng", itemNode.get("point").get("x").asDouble());
                        responseNode.withArray("results").add(responseResultItemNode);
                    }
                }
            }
        }

        log.info("응답은 처리댐");
        return ResponseEntity.ok().body(responseNode);
    }
}
