package com.zhuolu.cmd.cmdspringboot.autoconfigure;

import com.zhuolu.cmd.cmdspringboot.autoconfigure.bean.ApplicationContextAddStartProcess;
import com.zhuolu.cmd.cmdspringboot.autoconfigure.bean.ApplicationContextExportContext;
import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.net.server.CmdServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(CmdProperties.class)
public class CmdAutoConfigure {
    @Autowired
    private List<CmdStartProcess> processes;

    @Bean
    public CmdServer cmdSocketServer(CmdProperties cmdProperties) throws InterruptedException {
        Integer port = 5200;
        Integer lineSize = 1024;
        Integer boosSize = -1;
        Integer workerSize = -1;
        if (cmdProperties.getPort() != null && cmdProperties.getPort() > 0) {
            port = cmdProperties.getPort();
        }
        if (cmdProperties.getLineSize() != null && cmdProperties.getLineSize() > 1024) {
            lineSize = cmdProperties.getLineSize();
        }
        if (cmdProperties.getBoosSize() != null && cmdProperties.getBoosSize() > 0) {
            boosSize = cmdProperties.getBoosSize();
        }
        if (cmdProperties.getWorkerSize() != null && cmdProperties.getWorkerSize() > 0) {
            workerSize = cmdProperties.getWorkerSize();
        }
        CmdServer cmdServer = CmdServer.getBuilder()
                .processes(processes)
                .port(port)
                .lineSize(lineSize)
                .parentChildGroupSize(boosSize, workerSize)
                .build();
        cmdServer.run();
        return cmdServer;
    }

    @Bean
    public ApplicationContextExportContext applicationContextExportContext() {
        return new ApplicationContextExportContext();
    }

    @Bean
    public CmdStartProcess applicationContextAddStartProcess() {
        ApplicationContextAddStartProcess process = new ApplicationContextAddStartProcess();
        process.setExportContext(applicationContextExportContext());
        return process;
    }
}
