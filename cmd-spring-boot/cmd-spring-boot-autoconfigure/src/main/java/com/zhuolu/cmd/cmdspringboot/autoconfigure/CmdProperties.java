package com.zhuolu.cmd.cmdspringboot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = CmdProperties.CMD_PREFIX)
@Data
public class CmdProperties {
    static final String CMD_PREFIX = "cmd";

    /**
     * cmd export port
     */
    private Integer port;

    /**
     * cmd max line size
     */
    private Integer lineSize;

    /**
     * netty boosSize
     */
    private Integer boosSize;

    /**
     * netty workerSize
     */
    private Integer workerSize;
}
