appendwar-maven-plugin
======================

This is the readme file for appendwar-maven-plugin

This project contains a maven plugin that allow append to web.xml another fragments of other web.xml.

How To Use It?
-----------------------
You can use this library with maven, you have to add the repository in your pom.xml like this:

	<repository>
		<id>cloudbees-public-release-repository</id>
		<name>Gerard Ribas Releases</name>
		<url>http://repository-gribas.forge.cloudbees.com/release/</url>
		<layout>default</layout>
	</repository>
  
    <build>
      ....
      <plugins>
        <plugin>
          <groupId>es.gerardribas</groupId>
            <artifactId>appendwar-maven-plugin</artifactId>
            <version>1.0.0-RELEASE</version>
        </plugin>    
        ....
      </plugins>
      ....
    </build>

Now you are able to run maven with the appendwar goal webxml:

  mvn appendwar-maven-plugin:webxml
  
And build the new concatenated web.xml.

If you want to add the plugin in the maven lifecyle it´s possible wiht the executions tag in your pom.xml, for example:

    <plugin>
      <groupId>es.gerardribas</groupId>
      <artifactId>appendwar-maven-plugin</artifactId>
      <version>1.0.0-SNAPSHOT</version>
      <executions>
        <execution>
          <id>execution</id>
          <phase>package</phase>
          <goals>
            <goal>webxml</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

And now the appendwar plugin will be fired in Package phase.

Default configuration
---------------------

You can override the configuration in your pom. The default valuess are:

    outputDirectory =${project.build.directory}
    warName = ${project.build.finalName}
    webxml = ${project.build.directory}/${project.build.finalName}/WEB-INF/web.xml
    parentWebXml = ${project.build.directory}/${project.build.finalName}/WEB-INF/parent-web.xml
    destWebXmlAppended = ${project.build.directory}/${project.build.finalName}/WEB-INF/web.xml
    includeInWar = true
    deleteParentInWar = true
    classifier = null



Please check the packages of test. You can see how it works.

I hope my code helps you! 

Thanks for watching!
