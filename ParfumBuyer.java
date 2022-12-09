package mini_projet_iad;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ParfumBuyer extends Agent{
    ParallelBehaviour parallelBehaviour;
    int requesterCount;
    @Override
    protected void setup() {
        System.out.println("Salut je suis l'agent Acheteur mon nom est:"+this.getAID().getName());
        parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                MessageTemplate template=MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage aclMessage=receive(template);
                if(aclMessage!=null){
                    String parfum=aclMessage.getContent();
                    AID requester=aclMessage.getSender();
                    ++requesterCount;
                    String conversationID="transaction_"+parfum+"_"+requesterCount;
                    System.out.println("Conversion ID : "+conversationID);
                    parallelBehaviour.addSubBehaviour(
                            new RequestBehaviour(myAgent,parfum,requester,conversationID));
                }
                else
                    block();
            }
        });
    }
    @Override
    protected void beforeMove() {
        System.out.println("Avant de migrer vers une nouvelle location .....");
    }
    @Override
    protected void afterMove() {
        System.out.println("Je viens d'arriver à une nouvelle locatiion...");
    }
    @Override
    protected void takeDown() {
        System.out.println("avant de mourir .....");
    }
}

