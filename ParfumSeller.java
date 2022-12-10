package mini_projet_iad;

import java.util.HashMap;
import java.util.Map;import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
public class ParfumSeller extends Agent {
    private Map<String, Double> data=new HashMap();
    private ParallelBehaviour parallelBehaviour;
    @Override
    protected void setup() {
        data.put("BALEA", 230.0);
        data.put("DIAMOND", 460.0);
        data.put("ALEX", 540.0);
        data.put("LAROSA", 250.0);
        data.put("NIVEA", 800.0);
        data.put("SOUPLESSE", 500.0);

        System.out.println("....... Vendeur "+this.getAID().getName());
        System.out.println("--------------");
        System.out.println("Publication du service dans Directory Facilitator...");
        //enregistrer le parfum selling serive en page jaune
        //creer un description de l'agent
        DFAgentDescription agentDescription=new DFAgentDescription();
        //donner le nom de l'agent
        System.out.println("AID : "+this.getAID());
        agentDescription.setName(this.getAID());
        // decrire le service
        ServiceDescription serviceDescription=new ServiceDescription();
        //donner le type et le nom de service
        serviceDescription.setType("parfum-selling");
        serviceDescription.setName("parfum-selling");
        //ajouter le service à la description du agent
        agentDescription.addServices(serviceDescription);
        try {
            //publier le service dans l'annuaire
            DFService.register(this, agentDescription);
        } catch (FIPAException e1) {
            e1.printStackTrace();
        }
        parallelBehaviour=new ParallelBehaviour();
        addBehaviour(parallelBehaviour);
        parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {
            @Override public void action() {
                try {
                    MessageTemplate messageTemplate= MessageTemplate.MatchPerformative(ACLMessage.CFP); //receive cfp
                    ACLMessage aclMessage=receive(messageTemplate);
                    if(aclMessage!=null){
                        System.out.println("Conversation ID:"+aclMessage.getConversationId());
                        String parfum=aclMessage.getContent();
                        Double prix=data.get(parfum);
                        ACLMessage reply=aclMessage.createReply();

                        //seller send propose
                        reply.setPerformative(ACLMessage.PROPOSE);
                        reply.setContent(prix.toString());
                        System.out.println("...... En cours");
                        Thread.sleep(5000);send(reply);
                        //confirm propose
                        parallelBehaviour.addSubBehaviour(new SellerBehaviour(myAgent,aclMessage.getConversationId()));
                    }
                    else{
                        block();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        }
        catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
