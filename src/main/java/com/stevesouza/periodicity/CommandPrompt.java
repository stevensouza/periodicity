package com.stevesouza.periodicity;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import org.jline.utils.AttributedString;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommandPrompt implements PromptProvider {
    @Override
    public AttributedString getPrompt() {
        return new AttributedString("periodicity:> ");
    }
}
