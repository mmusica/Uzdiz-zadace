package org.foi.uzdiz.mmusica.utils.responsibility_chain;

public class SPVCommandHandler extends UserCommandHandler{
    public SPVCommandHandler(UserCommandHandler next) {
        super(next);
    }

    @Override
    void executeCommand(String[] commandArray) {
    }

    @Override
    String getCommand() {
        return null;
    }
}
