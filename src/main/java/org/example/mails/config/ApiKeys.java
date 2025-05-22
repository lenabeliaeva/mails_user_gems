package org.example.mails.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * I missed the difference between representatives and customers
 * and started thinking about representatives handling quite late,
 * so I keep representatives emails and tokens like that,
 * but I am sure it should be handled differently
 */
public final class ApiKeys {
    private static Map<String, String> apiKeys;

    public static String getToken(String email) {
        if (apiKeys == null) {
            fillApiKeys();
        }
        return apiKeys.get(email);
    }

    public static List<String> getRepresentativeEmails() {
        if (apiKeys == null) {
            fillApiKeys();
        }
        return new ArrayList<>(apiKeys.values());
    }

    private static void fillApiKeys() {
        apiKeys = new HashMap<>();
        apiKeys.put("7S$16U^FmxkdV!1b", "stephan@usergems.com");
        apiKeys.put("Ay@T3ZwF3YN^fZ@M", "christian@usergems.com'");
        apiKeys.put("PK7UBPVeG%3pP9%B", "joss@usergems.com");
        apiKeys.put("c0R*4iQK21McwLww", "blaise@usergems.com");
    }
}
