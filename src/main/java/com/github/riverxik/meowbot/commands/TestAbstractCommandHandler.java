package com.github.riverxik.meowbot.commands;

public class TestAbstractCommandHandler extends AbstractCommand {
    public TestAbstractCommandHandler() {
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("test");
    }
}
