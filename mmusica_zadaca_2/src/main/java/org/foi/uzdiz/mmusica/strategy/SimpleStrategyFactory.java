package org.foi.uzdiz.mmusica.strategy;

import org.foi.uzdiz.mmusica.utils.TerminalCommandHandler;

public class SimpleStrategyFactory {
    public DeliveryStrategy getStrategy(){
        String isporuka = (String) TerminalCommandHandler.getInstance().getNewProperties().get("isporuka");
        isporuka = isporuka.trim();
        if(isporuka.equals("1")){
            System.out.println("1. Nacin isporuke - prema redoslijedu ukrcavanja");
            return new InOrderDeliveryStrategy();
        }else if(isporuka.equals("2")){
            System.out.println("2. Nacin isporuke - najblizi paket");
            return new ClosestDeliveryStrategy();
        }else {
            System.out.println("Neispravan broj isporuke, postavljam se na isporuku " +
                    "1. redoslijed isporuke prema redoslijedu ukrcavanja");
            return new InOrderDeliveryStrategy();
        }
    }
}
