package es.tid.topologyModuleBase;

import es.tid.topologyModuleBase.database.TopologiesDataBase;
import es.tid.topologyModuleBase.plugins.TMPlugin;
import es.tid.topologyModuleBase.plugins.reader.TopologyReaderBGPLS;
import es.tid.topologyModuleBase.plugins.reader.TopologyReaderCOP;
import es.tid.topologyModuleBase.plugins.reader.TopologyReaderOSPF;
import es.tid.topologyModuleBase.plugins.reader.TopologyReaderXML;
import es.tid.topologyModuleBase.plugins.readerwriter.TopologyReaderWriterBGPLS;
import es.tid.topologyModuleBase.plugins.writer.*;
import es.tid.topologyModuleBase.session.ws.WSOldSession;

import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;


public class TMModuleInitiater {
	Logger log=Logger.getLogger("TMController");
			
	TopologiesDataBase ted;
	TopologyModuleParamsArray params;
	Lock lock;
	ArrayList<TMPlugin> pluginsList;
	ReaderParamsEnv envfile;



	private ScheduledThreadPoolExecutor executor;
	
	public TMModuleInitiater(TopologiesDataBase ted, TopologyModuleParamsArray params, Lock lock, ArrayList<TMPlugin> pluginsList)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
		this.pluginsList=pluginsList;
	}
	public void intiate() throws FileNotFoundException {
		executor = new ScheduledThreadPoolExecutor(20);
		ArrayList<TopologyModuleParams> paramList = params.getParamList();

		for (int i = 0; i < paramList.size(); i++) {
			TopologyModuleParams actualLittleParams = paramList.get(i);

			if (actualLittleParams.isEnv())
			{
				envfile = new ReaderParamsEnv(actualLittleParams);
				System.out.println(" Reading the Environment File for MdO ");
				break;
			}

		}



		for (int i = 0; i < paramList.size(); i++)
		{
			TopologyModuleParams actualLittleParams = paramList.get(i);
			//IMPORTERS
			
			if (actualLittleParams.isCOPReading())
			{
				TMPlugin p = new TopologyReaderCOP(ted, actualLittleParams,lock, envfile);
				executor.scheduleWithFixedDelay(p, 0,20, TimeUnit.SECONDS);
				pluginsList.add(p);
			}
			if (actualLittleParams.isXML())
			{
				TMPlugin p = new TopologyReaderXML(ted, actualLittleParams,lock, envfile);
				System.out.println("Andrea..............................Topology Reader");
				executor.execute(p);
				pluginsList.add(p);
				//log.info("Topology Read from file. State:\n"+ted.printTopology());
			}

			if (actualLittleParams.isOSPF())
			{
				TMPlugin p = new TopologyReaderOSPF(ted, actualLittleParams, lock, envfile);
				executor.execute(p);
				pluginsList.add(p);
			}
			
			/*if (actualLittleParams.isFloodLight())
			{
				(new TopologyReaderController(ted, actualLittleParams, lock)).readTopology();
			}
			
			if (actualLittleParams.isRestInfinera())
			{
				(new TopologyReaderInfinera(ted, actualLittleParams,lock)).readTopology();
			}*/
			
			if (actualLittleParams.isBGPLSReading())
			{
				TMPlugin p = new TopologyReaderBGPLS(ted, actualLittleParams,lock, envfile);
				executor.execute(p);
				pluginsList.add(p);
			}
			
			
			//EXPORTERS
			if (actualLittleParams.isBGPLSWriting())
			{
				log.info("Exporter BGPLS TED State:\n"+ted.printTopology());
				TMPlugin p = new TopologyServerBGPLS(ted, actualLittleParams,lock, envfile);
				executor.execute(p);
				pluginsList.add(p);
			}
			
			if (actualLittleParams.isGSON())
			{
				TMPlugin p = new TopologyServerGson(ted, actualLittleParams,lock);
				executor.execute(p);
				pluginsList.add(p);
			}
			if (actualLittleParams.isWSOld()){
				try {
					log.info("WSOld.  ParamsSize: "+paramList.size()+" Time: "+i+" Info: "+actualLittleParams.getIpWSOld()+":"+actualLittleParams.getPortWSOld());
					ServerSocket s= new ServerSocket(actualLittleParams.getPortWSOld());
					while (true){
						WSOldSession wssession= new WSOldSession(s.accept()	,ted);
						wssession.start();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (actualLittleParams.isCOPWriting())
			{
				TMPlugin p = new TopologyServerCOP(ted, actualLittleParams,lock);
				executor.execute(p);
				pluginsList.add(p);
			}
			
			if (actualLittleParams.isIETFWritting())
			{
				TMPlugin p = new TopologyServerIETF(ted, actualLittleParams,lock);
				executor.execute(p);
				pluginsList.add(p);
			}
			if (actualLittleParams.isUnifyWritting())
			{
				TMPlugin p = new TopologyServerUnify(ted, actualLittleParams,lock);
				executor.execute(p);
				pluginsList.add(p);
			}
			if (actualLittleParams.isTAPIWriting())
			{
                            log.info("vamos por aqui");
				TMPlugin p = new TopologyServerTAPI(ted, actualLittleParams,lock);
				executor.execute(p);
				pluginsList.add(p);
			} else {
                            System.out.println("VALOR DEL FLAG: " + actualLittleParams.isTAPIWriting());
                        }
                        
                        
			// IMPORTER/EXPORTER
			if (actualLittleParams.isBGPLSReadingWriting())
			{
				TMPlugin p = new TopologyReaderWriterBGPLS(ted, actualLittleParams,lock, envfile);
				executor.execute(p);
				pluginsList.add(p);
			}


			//BOTH
			/*NO WS
			 if (actualLittleParams.isWSOld()){
				try {
					log.info("WSOld.  ParamsSize: "+paramList.size()+" Time: "+i+" Info: "+actualLittleParams.getIpWSOld()+":"+actualLittleParams.getPortWSOld());
					ServerSocket s= new ServerSocket(actualLittleParams.getPortWSOld());
					while (true){
						WSOldSession wssession= new WSOldSession(s.accept()	,ted);
						wssession.start();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			*/
		}
		
		
		
	}

}
