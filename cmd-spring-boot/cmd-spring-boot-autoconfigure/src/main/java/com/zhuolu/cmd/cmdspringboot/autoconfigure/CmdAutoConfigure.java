package com.zhuolu.cmd.cmdspringboot.autoconfigure;

import com.zhuolu.cmd.core.entry.process.CmdStartProcess;
import com.zhuolu.cmd.net.socket.CmdSocketServer;
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
    public CmdSocketServer cmdSocketServer(CmdProperties cmdProperties) {
        CmdSocketServer cmdSocketServer = new CmdSocketServer();
        cmdSocketServer.setProcesses(processes);
        int port = 5200;
        if (cmdProperties.getPort() != null) {
            port = cmdProperties.getPort();
        }
        cmdSocketServer.run(port);
        return cmdSocketServer;
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
