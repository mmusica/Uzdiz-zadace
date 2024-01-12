package org.foi.uzdiz.mmusica.utils;

import org.foi.uzdiz.mmusica.utils.responsibility_chain.*;

public class UserCommandHandlerClient {
    public void handleUserCommand(String command) {
        String[] commandArray = command.split(" ");
        WIPCommandHandler wipCommandHandler = new WIPCommandHandler(null);
        CIPCommandHandler cipCommandHandler = new CIPCommandHandler(wipCommandHandler);
        SPVCommandHandler spvCommandHandler = new SPVCommandHandler(cipCommandHandler);
        PPVCommandHandler ppvCommandHandler = new PPVCommandHandler(spvCommandHandler);
        UnProxyCommandHandler unProxyCommandHandler = new UnProxyCommandHandler(ppvCommandHandler);
        ProxyCommandHandler proxyCommandHandler = new ProxyCommandHandler(unProxyCommandHandler);
        AdminCommandHandler adminCommandHandler = new AdminCommandHandler(proxyCommandHandler);
        PSCommandHandler psCommandHandler = new PSCommandHandler(adminCommandHandler);
        POCommandHandler poCommandHandler = new POCommandHandler(psCommandHandler);
        PPCommandHandler ppCommandHandler = new PPCommandHandler(poCommandHandler);
        VSCommandHandler vsCommandHandler = new VSCommandHandler(ppCommandHandler);
        VVCommandHandler vvCommandHandler = new VVCommandHandler(vsCommandHandler);
        SVCommandHandler svCommandHandler = new SVCommandHandler(vvCommandHandler);
        VRCommandHandler vrCommandHandler = new VRCommandHandler(svCommandHandler);
        UserCommandHandler userCommandHandler = new IPCommandHandler(vrCommandHandler);
        userCommandHandler.handleCommand(commandArray);
    }
}
