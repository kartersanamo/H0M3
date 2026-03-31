package com.sanamo.h0M3.commands;

import com.sanamo.h0M3.api.chat.ChatFormat;
import com.sanamo.h0M3.api.command.CommandContext;
import com.sanamo.h0M3.api.command.CoreCommand;
import com.sanamo.h0M3.api.util.MessagesUtil;
import com.sanamo.h0M3.api.util.PlaceholderUtil;

public class H0M3Command extends CoreCommand {
    public H0M3Command() {
        super(
                "h0m3",
                "Core plugin commands",
                "/h0m3 <reload>"
        );
    }

    @Override
    protected boolean onExecute(CommandContext context) {
        context.getSender().sendMessage(ChatFormat.info(
                PlaceholderUtil.replace(MessagesUtil.commandUsage, "%usage%", this.getUsage())
        ));
        return true;
    }
}
