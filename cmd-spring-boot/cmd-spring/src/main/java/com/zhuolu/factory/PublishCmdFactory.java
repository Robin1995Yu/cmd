package com.zhuolu.factory;

import com.zhuolu.cmd.PublishCmd;
import com.zhuolu.cmd.core.CmdRuntime;
import com.zhuolu.cmd.core.entry.cmd.Cmd;
import com.zhuolu.cmd.core.factory.CmdFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class PublishCmdFactory implements CmdFactory, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public String getName() {
        return "publish";
    }

    @Override
    public Cmd getCmd(Cmd previous, List<String> param, CmdRuntime cmdRuntime) {
        return new PublishCmd(previous, param, cmdRuntime, applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
