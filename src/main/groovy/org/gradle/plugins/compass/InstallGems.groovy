package org.gradle.plugins.compass

import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction;

class InstallGems extends JRubyTask {

	@OutputDirectory
	File gemPath

	InstallGems() {
		doFirst {
			getGemPath().mkdirs()
		}
	}
    
    @TaskAction
    void installGems() {
        for (gem in getRubyGems()) {
            jrubyexec(getJRubyArguments(gem))
        }
    }

	protected Iterable<String> getJRubyArguments(RubyGem rubyGem) {
		def args = []
		args << '-X-C'
		args << '-S' << 'gem' << 'install'
		args << '-i' << getGemPath()
		args << '--no-rdoc'
		args << '--no-ri'
            args << rubyGem.name
        if (rubyGem.version != null) {
            args << '-v'
            args << rubyGem.version
        }
		return args
	}
}
