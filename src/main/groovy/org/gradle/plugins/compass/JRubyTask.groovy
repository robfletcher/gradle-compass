package org.gradle.plugins.compass

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import static org.gradle.plugins.compass.CompassPlugin.CONFIGURATION_NAME

abstract class JRubyTask extends DefaultTask {

  String encoding

  @Input
  Collection<String> gems

  @TaskAction
  void jrubyexec() {
    project.javaexec {
      classpath = project.configurations[CONFIGURATION_NAME]
      main = 'org.jruby.Main'
      jvmArgs "-client -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Dfile.encoding=${getEncoding()}".tokenize()
      environment 'GEM_PATH', getGemPath()
      environment 'PATH', "${getGemPath()}/bin"
      args getJRubyArguments()
    }
  }

  abstract File getGemPath()

  protected abstract Iterable<String> getJRubyArguments()
}
