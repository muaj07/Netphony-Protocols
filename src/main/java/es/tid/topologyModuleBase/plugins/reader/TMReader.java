package es.tid.topologyModuleBase.plugins.reader;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import es.tid.topologyModuleBase.ReaderParamsEnv;
import es.tid.topologyModuleBase.TopologyModuleParams;
import es.tid.topologyModuleBase.database.TopologiesDataBase;

public class TMReader 
{
	public static String initFromXML = "initFromXML";
	public static String initFromOSPF = "initFromOSPF";
	public static String initFromBGPLS = "initFromBGPLS";
	public static String initFromFloodlight = "initFromFloodlight";
	public static String initFromRestInfinera = "initFromRestInfinera";
	
	Logger log=Logger.getLogger("TMController");
			
	TopologiesDataBase ted;
	TopologyModuleParams params;
	Lock lock;
	ReaderParamsEnv envfile;
	
	public TMReader(TopologiesDataBase ted, TopologyModuleParams params, Lock lock, ReaderParamsEnv envfile)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
		this.envfile= envfile;
	}
	public void read() throws IOException {
		String[] initFrom = params.getInitFrom();
		for (int i = 0; i < initFrom.length; i++)
		{
			if (initFrom[i].equals(TMReader.initFromXML))
			{
				(new TopologyReaderXML(ted, params,lock, envfile)).readTopology();
			}
			if (initFrom[i].equals(TMReader.initFromOSPF))
			{
				(new TopologyReaderOSPF(ted, params, lock, envfile)).readTopology();
			}
			if (initFrom[i].equals(TMReader.initFromBGPLS))
			{
				(new TopologyReaderBGPLS(ted, params, lock, envfile)).readTopology();
				}
			/*if (initFrom[i].equals(TMReader.initFromFloodlight))
			{
				(new TopologyReaderController(ted, params, lock)).readTopology();
			}
			if (initFrom[i].equals(TMReader.initFromRestInfinera))
			{
				(new TopologyReaderInfinera(ted, params,lock)).readTopology();
			}*/
		}
	}
}
