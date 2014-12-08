package com.github.robfletcher.compass

class TaskIntegrationSpec extends CompassPluginSpec {

  def setup() {
    buildFile << """
      apply plugin: "groovy"
    """
  }

  def "compassCompile is run as part of the assemble task"() {
    given:
    file("src/main/sass/main.scss") << '''
      body { font-family: Georgia, serif; }
    '''

    when:
    runTasks "assemble"

    then:
    wasExecuted ":compassCompile"
  }
}
