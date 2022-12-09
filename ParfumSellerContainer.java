package mini_projet_iad;
import java.util.Scanner;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class ParfumSellerContainer {
    public static void main(String[] args) {
        try {
            Runtime runtime=Runtime.instance();
            ProfileImpl profileImpl=new ProfileImpl(false);
            profileImpl.setParameter(ProfileImpl.MAIN_HOST,"localhost");
            AgentContainer agentContainer=runtime.createAgentContainer(profileImpl);
            AgentController agentController=agentContainer.createNewAgent("Vendeur1", "mini_projet_iad.ParfumSeller", new Object[]{});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
