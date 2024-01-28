package com.CloudStore.controllers;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SpringSessionController {

    @GetMapping("/")
    public Map<String, Object> home(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) session.getAttribute("MY_SESSION_MESSAGES");

        if (messages == null) {
            messages = new ArrayList<>();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("sessionMessages", messages);
        response.put("sessionId", session.getId());

        return response;
    }

    @PostMapping("/persistMessage")
    public Map<String, Object> persistMessage(@RequestParam("msg") String msg, HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        List<String> msgs = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
        if (msgs == null) {
            msgs = new ArrayList<>();
            request.getSession().setAttribute("MY_SESSION_MESSAGES", msgs);
        }
        msgs.add(msg);
        request.getSession().setAttribute("MY_SESSION_MESSAGES", msgs);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Message added");
        response.put("sessionId", request.getSession().getId());

        return response;
    }

    @PostMapping("/destroy")
    public Map<String, String> destroySession(HttpServletRequest request) {
        request.getSession().invalidate();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Session destroyed");

        return response;
    }
}
