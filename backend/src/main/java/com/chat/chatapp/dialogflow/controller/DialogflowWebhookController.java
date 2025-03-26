package com.chat.chatapp.dialogflow.controller;

import com.chat.chatapp.dialogflow.model.Ticket;
import com.chat.chatapp.dialogflow.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.cloud.firestore.*;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.*;

@RestController
@RequestMapping("/webhook")
public class DialogflowWebhookController {

    private Firestore db;

    @PostConstruct
    public void init() throws Exception {
        FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> handleWebhook(@RequestBody Map<String, Object> body) {
        String session = (String) body.get("session");
        Map<String, Object> queryResult = (Map<String, Object>) body.get("queryResult");
        Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");

        String incidentId = (String) parameters.get("incident_id");

        Map<String, String> ticket = getTicketDetails(incidentId);

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> fulfillmentResponse = new HashMap<>();
        List<Map<String, Object>> messages = new ArrayList<>();

        if (ticket != null) {
            messages.add(Map.of("text", Map.of("text", List.of("Incident ID: " + incidentId + "\nStatus: " + ticket.get("status") + "\nDescription: " + ticket.get("description")))));
        } else {
            messages.add(Map.of("text", Map.of("text", List.of("Incident ID not found."))));
        }

        fulfillmentResponse.put("messages", messages);
        response.put("fulfillment_response", fulfillmentResponse);

        return ResponseEntity.ok(response);
    }

    private Map<String, String> getTicketDetails(String incidentId) {
        try {
            DocumentReference docRef = db.collection("tickets").document(incidentId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Map<String, Object> data = document.getData();
                return Map.of(
                        "id", incidentId,
                        "status", (String) data.get("status"),
                        "description", (String) data.get("description")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


