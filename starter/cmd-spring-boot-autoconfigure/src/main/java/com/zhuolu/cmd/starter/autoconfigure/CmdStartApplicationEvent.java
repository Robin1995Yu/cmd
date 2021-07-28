package com.zhuolu.cmd.starter.autoconfigure;

import org.springframework.context.ApplicationEvent;

public class CmdStartApplicationEvent extends ApplicationEvent {
    public CmdStartApplicationEvent(Object source) {
        super(source);
    }
}
