package es.gerardribas.appendwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;

import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;

public class WebXmlAppenderMojoTestCase {

	private File war;

	private File webXml;

	private File parentWebXml;

	private File resultWebXml;

	private WebXmlAppenderMojo getWebXmlAppenderMojo() {
		war = new File(WebXmlAppenderMojoTestCase.class.getClassLoader()
				.getResource("example-war-0.0.1-SNAPSHOT.war").getFile());

		webXml = new File(WebXmlAppenderMojoTestCase.class.getClassLoader()
				.getResource("web.xml").getFile());

		parentWebXml = new File(WebXmlAppenderMojoTestCase.class
				.getClassLoader().getResource("parent-web.xml").getFile());

		resultWebXml = new File(war.getParent() + "/result-web.xml");

		WebXmlAppenderMojo result = new WebXmlAppenderMojo();
		result.setDestWebXmlAppended(resultWebXml);
		result.setOutputDirectory(war.getParent());
		result.setParentWebXml(parentWebXml);
		result.setWarName("example-war-0.0.1-SNAPSHOT");
		result.setWebxml(webXml);
		return result;
	};

	@Test
	public void testAppendXml() throws MojoExecutionException, FileNotFoundException, IOException {
		WebXmlAppenderMojo mojo = getWebXmlAppenderMojo();
		mojo.setIncludeInWar(true);
		mojo.execute();
		
		TFile file = new TFile(war.getAbsoluteFile() + "/WEB-INF/web.xml");
		
		TFile parent = new TFile(war.getAbsoluteFile() + "/WEB-INF/" + parentWebXml.getName());
		Assert.assertFalse(parent.exists());
		
		String webxmlvalue = readFileAsString(new TFileInputStream(file));
		Assert.assertFalse(webxmlvalue.contains("<!-- NOTHING HERE -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- WEB XML FROM APPLICATION -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- END WEB XML FROM APPLICATION -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- WEB XML FROM PARENT -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- END WEB XML FROM PARENT -->"));
	}
	
	@Test
	public void testWebXml() throws MojoExecutionException, FileNotFoundException, IOException {
		WebXmlAppenderMojo mojo = getWebXmlAppenderMojo();
		mojo.setIncludeInWar(false);
		mojo.execute();
		
		String webxmlvalue = readFileAsString(new FileInputStream(resultWebXml));
		Assert.assertFalse(webxmlvalue.contains("<!-- NOTHING HERE -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- WEB XML FROM APPLICATION -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- END WEB XML FROM APPLICATION -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- WEB XML FROM PARENT -->"));
		Assert.assertTrue(webxmlvalue.contains("<!-- END WEB XML FROM PARENT -->"));
	}
	
	 
    private static String readFileAsString(InputStream file)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

}
