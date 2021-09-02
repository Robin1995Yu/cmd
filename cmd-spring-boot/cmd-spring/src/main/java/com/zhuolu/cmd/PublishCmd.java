package com.zhuolu.cmd;

import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.AbstractCmd;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class PublishCmd extends AbstractCmd {
    private final ApplicationContext applicationContext;
    private static final Class<ApplicationEvent> DEFAULT_APPLICATION_EVENT_TYPE = ApplicationEvent.class;

    public PublishCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime, ApplicationContext applicationContext) {
        super("publish", previous, param, cmdRuntime);
        this.applicationContext = applicationContext;
    }

    @Override
    protected void init() {

    }
}
