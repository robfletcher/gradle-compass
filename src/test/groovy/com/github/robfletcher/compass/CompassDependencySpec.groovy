package com.github.robfletcher.compass

class CompassDependencySpec extends CompassPluginSpec {

  def "by default uses latest compass version"() {
    when:
    runTasks "compassVersion"

    then:
    standardOutput.readLines().contains "Compass 1.0.3 (Polaris)"
  }

  def "can specify compass version"() {
    given:
    buildFile << """
      dependencies {
        compass "rubygems:compass:$version"
      }
    """

    when:
    runTasks "compassVersion"

    then:
    standardOutput.readLines().contains "Compass 0.12.6 (Alnilam)"

    where:
    version = "0.12.6"
  }

}
