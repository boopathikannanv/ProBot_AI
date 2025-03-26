package com.chat.chatapp.dialogflow.service;

import com.chat.chatapp.dialogflow.model.Ticket;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final List<Ticket> ticketCache = new ArrayList<>();

    @PostConstruct
    public void loadCSV() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("servicenow_tickets.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        reader.readLine(); // Skip header

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Ticket ticket = new Ticket();
            ticket.setTicketId(data[0]);
            ticket.setShortDescription(data[1]);
            ticket.setStatus(data[2]);
            ticket.setAssignee(data[3]);
            ticket.setOpenedDate(data[4]);
            ticketCache.add(ticket);
        }
    }

    public Optional<Ticket> findById(String ticketId) {
        return ticketCache.stream()
                .filter(t -> t.getTicketId().equalsIgnoreCase(ticketId))
                .findFirst();
    }
}
