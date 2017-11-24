package es.tid.topologyModuleBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import es.tid.topologyModuleBase.TopologyModuleParams;
/**
 * Created by Ajmal on 2017-10-25.
 */
public class ReaderParamsEnv {
    TopologyModuleParams params;

  String line;
  //Scanner scan ;
  String Identifier=null;
  String Domain_ID=null;


    public ReaderParamsEnv(){


    }


    public ReaderParamsEnv(TopologyModuleParams Params) throws FileNotFoundException {
        this.params=Params;
        Scanner scan = new Scanner(new File(params.getEnvirFile()));

        while (scan.hasNextLine()){
            line=scan.nextLine();
            if(line.contains("MDO_HOST"))
            {
                String[] val = line.split("//");
                //Identifier=val[1];
                params.setMdOIdentifier(val[1]);
                setIdentifier(val[1]);

            }

            else if(line.contains("DOMAIN_ID"))
            {
                String[] val = line.split("=");
                //Domain_ID=val[1];
                params.setDomainID(val[1]);
                setDomainID(val[1]);

            }
        }
    }

   public  void setIdentifier(String Identifier){

     this.Identifier= Identifier;

    }

    public String getIdentifier(){

        return this.Identifier;
    }

   public void setDomainID(String DomainID){

        this.Domain_ID= DomainID;

    }

    public String getDomainID(){

        return this.Domain_ID;
    }


}
