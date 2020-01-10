package com.github.robfletcher.compass

import groovy.json.JsonSlurper

class CompassDependencySpec extends CompassPluginSpec {

  def "by default uses latest compass version"() {
    given:
    def latestVersion = new JsonSlurper()
      .parseText("https://rubygems.org/api/v1/gems/compass.json".toURL().text)
      .version

    when:
    runTasksSuccessfully "compassVersion"

    then:
    standardOutput.readLines().any {
      it =~ /^Compass $latestVersion/
    }
  }

  def "can specify compass version"() {
    given:
    buildFile << """
      dependencies {
        compass "rubygems:compass:$version"
      }
    """

    when:
    runTasksSuccessfully "compassVersion"

    then:
    standardOutput.readLines().contains "Compass 0.12.6 (Alnilam)"

    where:
    version = "0.12.6"
  }

}
