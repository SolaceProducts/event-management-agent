package com.solace.maas.ep.event.management.agent.command.rest;

import com.solace.maas.ep.event.management.agent.command.CommandManager;
import com.solace.maas.ep.event.management.agent.plugin.command.model.CommandRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/v2/ema/command")

public class CommandController {
    private final CommandManager commandManager;

    public CommandController(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @PostMapping
    public ResponseEntity<CommandRequest> executeTfCommand(@RequestBody CommandRequest commandRequest) {
        commandManager.execute(commandRequest);
        return ResponseEntity.ok(commandRequest);
    }
}
