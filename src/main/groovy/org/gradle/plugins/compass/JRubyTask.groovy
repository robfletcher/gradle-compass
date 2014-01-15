package org.gradle.plugins.compass

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import static org.gradle.plugins.compass.CompassPlugin.CONFIGURATION_NAME

abstract class JRubyTask extends DefaultTask {

  @Input
  String encoding

  @Input
  String jvmArgs

  @Input
  Collection<String> gems

  void jrubyexec(Iterable<String> jrubyArgs) {
    project.javaexec {
      classpath = project.configurations[CONFIGURATION_NAME]
      main = 'org.jruby.Main'
      it.jvmArgs getCombinedArgs()
      environment 'GEM_PATH', getGemPath()
      environment 'PATH', "${getGemPath()}/bin"
      args jrubyArgs
    }
  }

  protected List<String> getCombinedArgs() {
    "${getJvmArgs()} -Dfile.encoding=${getEncoding()}".trim().tokenize()
  }

  protected Collection<RubyGem> getRubyGems() {
    getGems()?.collect { new RubyGem(it) }
  }

  abstract File getGemPath()
}
