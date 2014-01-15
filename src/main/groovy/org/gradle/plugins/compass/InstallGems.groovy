package org.gradle.plugins.compass

import org.gradle.api.tasks.OutputDirectory

class InstallGems extends JRubyTask {

  @OutputDirectory
  File gemPath

  InstallGems() {
    doFirst {
      getGemPath().mkdirs()
    }
  }

  protected Iterable<String> getJRubyArguments() {
    def args = []
    args << '-X-C'
    args << '-S' << 'gem' << 'install'
    args << '-i' << getGemPath()
    args << '--no-rdoc'
    args << '--no-ri'
    for (gem in getGems()) args << gem
    return args
  }
}
