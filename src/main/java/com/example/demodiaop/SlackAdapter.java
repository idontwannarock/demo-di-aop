package com.example.demodiaop;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

import java.io.IOException;

public class SlackAdapter implements Notification {

    @Override
    public void notify(String account, String message) {
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methodClient = slack.methods("slackToken");
            ChatPostMessageRequest messageRequest = ChatPostMessageRequest.builder()
                    .channel("#random")
                    .text(message)
                    .build();
            methodClient.chatPostMessage(messageRequest);
        } catch (IOException | SlackApiException e) {
            throw new AuthenticationException("Slack web api error, account: " + account, e);
        }
    }
}