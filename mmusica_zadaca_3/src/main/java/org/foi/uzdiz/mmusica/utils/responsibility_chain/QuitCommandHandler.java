package org.foi.uzdiz.mmusica.utils.responsibility_chain;

public class QuitCommandHandler extends UserCommandHandler {
    public QuitCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
    }

    @Override
    String getCommand() {
        return "Q";
    }
}
