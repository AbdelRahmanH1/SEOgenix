package com.system.seogenix.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Service
public class AiService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.openai.key}")
    private String key;

    private final String promptStart = """
            I will give you an article title. Your job is to generate 50 highly relevant, high-ranking SEO keywords or tags that match this title.
            Focus only on tags related to SEO and search intent. Avoid generic, irrelevant, or repetitive terms. No explanation—just return a clean list of 50 tags.
            Title:""";

    private static final List<String> AVAILABLE_MODELS = List.of(
            "qwen/qwen3-coder:free",
            "qwen/qwen3-235b-a22b-07-25:free",
            "moonshotai/kimi-k2:free",
            "google/gemma-3n-e2b-it:free",
            "tngtech/deepseek-r1t2-chimera:free"
    );

    public AiService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<List<String>> generateResponse(String prompt) {
        String fullPrompt = promptStart + prompt;
        return tryModels(fullPrompt, 0);
    }

    private Mono<List<String>> tryModels(String prompt, int index) {
        if (index >= AVAILABLE_MODELS.size()) {
            return Mono.just(List.of());
        }

        String model = AVAILABLE_MODELS.get(index);
        System.out.println("Trying model: " + model);

        return webClient.post()
                .uri("https://openrouter.ai/api/v1/completions")
                .header("Authorization", key)
                .header("Content-Type", "application/json")
                .bodyValue(buildPrompt(prompt, model))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractTags)
                .flatMap(tags -> {
                    if (tags.isEmpty()) {
                        System.out.println("Model responded but no valid tags from: " + model);
                        return tryModels(prompt, index + 1);
                    } else {
                        return Mono.just(tags);
                    }
                })
                .onErrorResume(error -> {
                    System.out.println("Model failed: " + model + " | Error: " + error.getMessage());
                    return tryModels(prompt, index + 1);
                });
    }


    private String buildPrompt(String fullPrompt, String model) {
        return """
                {
                  "model": "%s",
                  "prompt": "%s",
                  "max_tokens": 500,
                  "temperature": 0.7
                }
                """.formatted(model, fullPrompt.replace("\"", "\\\""));
    }

    private List<String> extractTags(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            String text = root.path("choices").get(0).path("text").asText().trim();

            if (text.startsWith("[") && text.endsWith("]")) {
                return objectMapper.readValue(text, new TypeReference<List<String>>() {});
            }

            return Arrays.stream(text.split("\n"))
                    .map(String::trim)
                    .filter(line -> line.matches("^\\d+\\.\\s+.+$"))
                    .map(line -> line.replaceFirst("^\\d+\\.\\s+", ""))
                    .toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
