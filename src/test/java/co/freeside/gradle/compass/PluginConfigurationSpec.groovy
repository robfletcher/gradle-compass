package co.freeside.gradle.compass

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class PluginConfigurationSpec extends Specification {

  def project = ProjectBuilder.builder().build()

  def setup() {
    project.apply plugin: "co.freeside.compass"
  }

  def "adds a default compass dependency if none is specified"() {
    expect:
    with(project.configurations.getByName("compass").dependencies.first()) {
      group == "rubygems"
      name == "compass"
      version == "+"
    }
  }

}
