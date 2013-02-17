/**
 * 
 */
package es.gerardribas.appendwar;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

import de.schlichtherle.truezip.file.TFile;


/**
 * @author Gerard
 * 
 */
public final class WarFileTool {

	private static WarFileTool instance;
	
	private static final String WEB_XML_LOCATION = "/WEB-INF/web.xml";

	private WarFileTool() {
	}

	public static WarFileTool getInstance() {
		if (instance == null) {
			instance = new WarFileTool();
		}
		return instance;
	}
	
	public void copyWebXmlToWar(File warFile, File webXmlToCopy, Log log) throws IOException{
		log.info("Replacing web.xml with new one...");
		TFile webXmlInWar = new TFile(warFile.getAbsolutePath() + WEB_XML_LOCATION);
		TFile.cp_p(webXmlToCopy, webXmlInWar);
		log.info("End Replacing web.xml with new one...");
	}
	public void deleteParentWebXml(File warFile, String parentWarName, Log log) throws IOException{
		log.info("Delete " + parentWarName +" in war");
		TFile parentWebInf = new TFile(warFile.getAbsolutePath() + "/WEB-INF/"+ parentWarName);
		TFile.rm(parentWebInf);
		log.info("End Delete " + parentWarName +" in war");
	}
}
