package es.tid.bgp.bgp4Peer.peer;

import com.google.common.net.InetAddresses;
import com.google.common.net.MediaType;
import es.tid.bgp.bgp4Peer.updateTEDB.CMS_Domain_Msgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;



/**
 * Created by Ajmal on 2017-09-27.
 */
public class CMSPostMsgs implements Runnable {
    private Logger log;
    private LinkedList<CMS_Domain_Msgs> cms_msg;
    private int Domainid;
    private Create_CMS_Post create_post ;
    private Boolean localDomain= false;
    private String BGPIdentifier;

    public void configure(LinkedList<CMS_Domain_Msgs> cms_msg, String BGPIdentifier)
    {

        log = LoggerFactory.getLogger("BGP4Peer");
        this.cms_msg=cms_msg;
        this.BGPIdentifier= BGPIdentifier;
    }

    @Override
    public void run() {

        log.info("Msgs to Send to Catalogue Management Subsystem  : " +cms_msg.size() + " BGP Identifer:  " +this.BGPIdentifier);

        //log.info("Msgs to Send to Catalogue Management Subsystem  : " +cms_msg.size());

        if(cms_msg.size()!=0)
        {
          for(int i=0; i< cms_msg.size(); i++)
          {
              if(cms_msg.get(i).getMsg_Flag()==false)
              {
                  //Create a Post Message for the Domain
                  //Domainid= (Inet4Address) Inet4Address.getByName( cms_msg.get(i).getDomainID());
                  //Domainid = cms_msg.get(i).getDomainID();
                  //int domain = InetAddresses.coerceToInteger(Domainid);
                  Domainid= cms_msg.get(i).getDomainID();
                  if(cms_msg.get(i).getlearntfrom().equals(this.BGPIdentifier))
                        localDomain= true;

                 //log.info("Learn from: "+cms_msg.get(i).getlearntfrom() +" BGP Identifier:  " +this.BGPIdentifier);

                 // log.info("Learn from: "+cms_msg.get(i).getlearntfrom() +" BGP Identifier:  " +this.BGPIdentifier);

                  //log.info("Entry point: "+cms_msg.get(i).getEntryPoint() +" Domain: " +cms_msg.get(i).getDomainID() +" Local Domain: " +localDomain);
                  create_post= new Create_CMS_Post(cms_msg.get(i).getEntryPoint(), Domainid, localDomain, this.BGPIdentifier);
                  cms_msg.get(i).setMsg_Flag(true); //Set the Flag to True
              }

          }

        }
    }




}
