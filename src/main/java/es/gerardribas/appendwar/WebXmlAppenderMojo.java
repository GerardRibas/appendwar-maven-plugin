package es.gerardribas.appendwar;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

@Mojo( name = "webxml", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true )
public class WebXmlAppenderMojo extends AbstractMojo {
	/**
	 * The directory for the generated WAR.
	 */
	@Parameter(defaultValue = "${project.build.directory}", required = true)
	private String outputDirectory;

	/**
	 * The name of the generated WAR.
	 */
	@Parameter(defaultValue = "${project.build.finalName}", required = true)
	private String warName;
	
	@Parameter
	private String classifier;
	
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/WEB-INF/web.xml", required = true)
	private File webxml;
	
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/WEB-INF/parent-web.xml", required = true)
	private File parentWebXml;
	
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}/WEB-INF/web.xml", required = true)
	private File destWebXmlAppended;
	
	@Parameter(defaultValue = "true" )
    private boolean includeInWar = true;
	
	@Parameter(defaultValue = "true" )
    private boolean deleteParentInWar = true;

	public void execute() throws MojoExecutionException {
		getLog().info("Starting to build new web.xml");
		try {
			WebXmlAppenderTool.getInstance().appendWebXml(webxml, parentWebXml, destWebXmlAppended, getLog());
			if(includeInWar){
				getLog().info("Start Including web.xml in war...");
				WarFileTool.getInstance().copyWebXmlToWar( getTargetWarFile(), destWebXmlAppended, getLog());
				getLog().info("End Including web.xml in war...");
			} else {
				getLog().warn("Excluding to include web.xml in war...");
			}
			if(deleteParentInWar){
				getLog().info("Start deleting parent web.xml in war...");
				WarFileTool.getInstance().deleteParentWebXml(getTargetWarFile(), parentWebXml.getName(), getLog());
				getLog().info("End deleting parent web.xml in war...");
			} else {
				getLog().warn("Excluding to include web.xml in war...");
			}
			
		} catch (Exception e) {
			getLog().error(e);
		}
		getLog().info("End build new web.xml");
	}

	protected static File getTargetFile(File basedir, String finalName,
			String classifier, String type) {
		String insideClassifier = null;
		if(StringUtils.isEmpty(classifier)){
			insideClassifier = "";
		} else {
			insideClassifier = classifier;
		}
		 if (insideClassifier.trim().length() > 0
				&& !insideClassifier.startsWith("-")) {
			insideClassifier = "-" + insideClassifier;
		}
		return new File(basedir, finalName + insideClassifier + "." + type);
	}

	protected File getTargetWarFile() {
		return getTargetFile(new File(outputDirectory), warName,
				classifier, "war");
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public String getWarName() {
		return warName;
	}

	public void setWarName(String warName) {
		this.warName = warName;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public File getWebxml() {
		return webxml;
	}

	public void setWebxml(File webxml) {
		this.webxml = webxml;
	}

	public File getParentWebXml() {
		return parentWebXml;
	}

	public void setParentWebXml(File parentWebXml) {
		this.parentWebXml = parentWebXml;
	}

	public File getDestWebXmlAppended() {
		return destWebXmlAppended;
	}

	public void setDestWebXmlAppended(File destWebXmlAppended) {
		this.destWebXmlAppended = destWebXmlAppended;
	}

	public boolean isIncludeInWar() {
		return includeInWar;
	}

	public void setIncludeInWar(boolean includeInWar) {
		this.includeInWar = includeInWar;
	}

	public boolean isDeleteParentInWar() {
		return deleteParentInWar;
	}

	public void setDeleteParentInWar(boolean deleteParentInWar) {
		this.deleteParentInWar = deleteParentInWar;
	}
}
