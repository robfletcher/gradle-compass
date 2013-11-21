package org.gradle.plugins.compass

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import static org.gradle.plugins.compass.CompassPlugin.CONFIGURATION_NAME

abstract class JRubyTask extends DefaultTask {
    
    @Input
    String encoding
    
    @Input
    String jvmArgs
    
    @Input
    Collection<String> gems

	@TaskAction
	void jrubyexec() {
		project.javaexec {
			classpath = project.configurations[CONFIGURATION_NAME]
			main = 'org.jruby.Main'
			it.jvmArgs getCombinedArgs()
			environment 'GEM_PATH', getGemPath()
			environment 'PATH', "${getGemPath()}/bin"
			args getJRubyArguments()
		}
	}
    
    protected List<String> getCombinedArgs() {
       "${getJvmArgs()} -Dfile.encoding=${getEncoding()}".trim().tokenize()
    }

	abstract File getGemPath()
	protected abstract Iterable<String> getJRubyArguments()
}
