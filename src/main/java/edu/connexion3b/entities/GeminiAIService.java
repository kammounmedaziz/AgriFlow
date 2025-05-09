//package edu.connexion3b.entities;
//import com.google.cloud.aiplatform.v1beta1.PredictionServiceClient;
//import com.google.cloud.aiplatform.v1beta1.RawPredictRequest;
//import com.google.api.HttpBody;
//import com.google.protobuf.Value;
//import com.google.protobuf.Struct;
//import com.google.protobuf.ListValue;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GeminiAIService {
//
//    private final String apiKey;
//    private final String endpoint = "us-central1-aiplatform.googleapis.com:443";
//    private final String modelName = "projects/ gen-lang-client-0430238183/locations/us-central1/models/gemini-pro";
//    public GeminiAIService(String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    public String generateText(String prompt) throws IOException {
//        // Prepare the instance payload
//        List<Value> instances = new ArrayList<>();
//
//        // Building the structured input
//        Value instance = Value.newBuilder().setStructValue(
//                Struct.newBuilder()
//                        .putFields("prompt", Value.newBuilder().setStringValue(prompt).build())
//                        .build()
//        ).build();
//
//        instances.add(instance);
//
//        // Create the RawPredictRequest
//        // Créez le RawPredictRequest avec le bon format
//        RawPredictRequest rawPredictRequest = RawPredictRequest.newBuilder()
//                .setEndpoint(endpoint) // L'endpoint de l'API
//                .setHttpBody(HttpBody.newBuilder()
//                        .setContentType("application/json")
//                        .setData(com.google.protobuf.ByteString.copyFromUtf8(
//                                "{\"instances\": [{\"prompt\": \"" + prompt + "\"}]}"
//                        ))
//                        .build()
//                )
//                .build();
//
//
//        // Sending the request
//        try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create()) {
//            HttpBody responseBody = predictionServiceClient.rawPredict(rawPredictRequest);
//
//            // Convert response to JSON string
//            String responseJson = responseBody.getData().toStringUtf8();
//            System.out.println("Response JSON: " + responseJson);
//
//            // Return the raw JSON response
//            return responseJson;
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        String apiKey = "AIzaSyBEHeH5I_7m2dtVmQf9NraBYrK4seYRARo";  // <-- Put your API Key here
//        GeminiAIService service = new GeminiAIService(apiKey);
//        String response = service.generateText("Explain how AI works in a few words");
//        System.out.println("Gemini Response: " + response);
//    }
//}
