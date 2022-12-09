package mini_projet_iad;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class RequestBehaviour  extends CyclicBehaviour{
    private String conversationID;
    private AID requester;
    private String parfum;
    private double prix;
    private int compteur;
    private List<AID> vendeurs=new ArrayList();
    private AID aid = new AID("vendeur1", AID.ISLOCALNAME);;
    private double meilleurPrix;
    private int index;
    public RequestBehaviour(Agent agent,String parfum,AID requester,String conversationID) {
        super(agent);
        this.parfum=parfum;
        this.requester=requester;
        this.conversationID=conversationID;
        System.out.println("Recherche des services...");
        System.out.println("agent..."+agent);

        vendeurs=chercherServices(myAgent," parfum-selling");
        System.out.println("Liste des vendeurs trouvés :");

        try {

            /*for(AID aid:vendeurs){
                System.out.println("==== "+aid.getName());
            }*/
            ++compteur;
            System.out.println("#########################################");
            System.out.println("Requête d'achat de parfum:");
            System.out.println("From :"+requester.getName());
            System.out.println("parfum : "+parfum);
            System.out.println("............................");
            System.out.println("Envoi de la requête....");
            ACLMessage msg=new ACLMessage(ACLMessage.CFP);
            msg.setContent(parfum);
            msg.setConversationId(conversationID);
            msg.addUserDefinedParameter(String.valueOf(compteur), String.valueOf(compteur));

            msg.addReceiver(aid);
            /*for(AID aid:vendeurs){
                msg.addReceiver(aid);
            } */
            System.out.println("....... En cours");
            Thread.sleep(5000);
            index=0;
            myAgent.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void action() {
        try {
            MessageTemplate template=
                    MessageTemplate.and( MessageTemplate.MatchConversationId(conversationID),
                            MessageTemplate.or( MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                                    MessageTemplate.MatchPerformative(ACLMessage.CONFIRM)));
            ACLMessage aclMessage=myAgent.receive(template);
            if(aclMessage!=null){
                switch(aclMessage.getPerformative()){
                    case ACLMessage.PROPOSE :
                        prix=Double.parseDouble(aclMessage.getContent());
                        System.out.println("***********************************");
                        System.out.println("Conversation ID:"+aclMessage.getConversationId());
                        System.out.println("Réception de l'offre :");
                        System.out.println("From :"+aclMessage.getSender().getName());
                        System.out.println("Prix="+prix);
                        meilleurPrix=prix;
                        System.out.println("-----------------------------------");
                        System.out.println("Conclusion de la transaction.......");
                        ACLMessage aclMessage2=new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        aclMessage2.addReceiver(aid);
                        aclMessage2.setConversationId(conversationID);
                        System.out.println("...... En cours");
                        Thread.sleep(5000);
                        myAgent.send(aclMessage2);
                        break;
                    case ACLMessage.CONFIRM:
                        System.out.println(".........................");
                        System.out.println("Reçu de la confirmation ...");
                        System.out.println("Conversation ID:"+aclMessage.getConversationId());
                        ACLMessage msg3=new ACLMessage(ACLMessage.INFORM);
                        msg3.addReceiver(requester);
                        msg3.setConversationId(conversationID);
                        msg3.setContent("<transaction>"+
                               "<parfum>"+parfum+"<parfum>"
                                + "<prix>"+meilleurPrix+"</prix>" +
                                "<fournisseur>"+aclMessage.getSender().getName()+"</fournisseur>"
                                +"</transaction>");
                        myAgent.send(msg3);
                        break;
                }
            }
            else{
                block();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //chercher les vendeurs qui ont le service parfum-selling
    public List<AID> chercherServices(Agent agent,String type){
        List<AID> vendeurs=new ArrayList();
        //creer un description de l'agent
        DFAgentDescription agentDescription=new DFAgentDescription();
        // decrire le service
        ServiceDescription serviceDescription=new ServiceDescription();
        //donner le type de service
        serviceDescription.setType(type);
        agentDescription.addServices(serviceDescription);
        try {
            DFAgentDescription[] descriptions = DFService.search(agent, agentDescription);
            for (DFAgentDescription dfad : descriptions) {
                {
                    vendeurs.add(dfad.getName());
                }
            }
        }catch (FIPAException e)
            {
                e.printStackTrace();
            }
        return vendeurs;

    }
}
