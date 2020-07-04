package com.vazgen.community.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    servers = {@Server(url = "http://localhost:8080")},
    info = @Info(title = "Community Connections API Definition", version = "1"))
public class APIDefinition {

}
