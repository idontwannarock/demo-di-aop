package com.example.demodiaop;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

import java.io.IOException;

public class SlackAdapter {

    void notify(String account) {
        try {
            Slack slack = Slack.getInstance();
            MethodsClient methodClient = slack.methods("slackToken");
            ChatPostMessageRequest messageRequest = ChatPostMessageRequest.builder().channel("#random").text("account: " + account + " failed to login.").build();
            methodClient.chatPostMessage(messageRequest);
        } catch (IOException | SlackApiException e) {
            throw new AuthenticationException("Slack web api error, account: " + account, e);
        }
    }
}