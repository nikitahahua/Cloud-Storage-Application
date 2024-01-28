package com.CloudStore.message.strategies;

import com.CloudStore.messages.Message;

public interface MessageStrategy {

    void processMessage(Message message);

}
