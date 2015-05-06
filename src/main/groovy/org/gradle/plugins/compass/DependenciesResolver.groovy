package org.gradle.plugins.compass

import org.gradle.api.tasks.*

class DependenciesResolver extends JRubyTask {

  String sourcesMirror

  @OutputDirectory
  File gemPath

    DependenciesResolver() {
    doFirst {
      getGemPath().mkdirs()
    }
  }

  @TaskAction
  void install() {
    if (sourcesMirror){
      logger.info('Adding source mirror.')
      def args = []
      args << '-S' << 'gem' << 'sources' << '--add' << sourcesMirror
      jrubyexec(args)
    }
    if (gemJars?.empty) {
        for (gem in getRubyGems()) {
            jrubyexec(getJRubyArguments(gem))
        }
    }
    else {
        addGemJarsDependencies()
    }
  }

  def addGemJarsDependencies() {
    for(String jarDependency:gemJars) {
        project.dependencies.add(CompassPlugin.CONFIGURATION_NAME, jarDependency)
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
