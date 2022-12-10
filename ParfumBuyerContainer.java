package mini_projet_iad;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.Scanner;

public class ParfumBuyerContainer {
    public static void main(String[] args) {
        try {
            Scanner clavier=new Scanner(System.in);
            System.out.print("Nom du acheteur:");
            String name=clavier.next();
            Runtime runtime=Runtime.instance();
            ProfileImpl profileImpl=new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
            AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
            AgentController agentController=
                    agentContainer.createNewAgent(name, "mini_projet_iad.ParfumBuyer", new Object[]{});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
