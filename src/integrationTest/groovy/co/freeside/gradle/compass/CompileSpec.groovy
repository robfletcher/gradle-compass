package co.freeside.gradle.compass

import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import org.gololang.gradle.test.integration.framework.IntegrationSpec
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSRuleList
import spock.lang.Ignore

class CompileSpec extends IntegrationSpec {

  final COMPILE_TASK_NAME = "compassCompile"

  protected static String localRepoLocation() {
    System.properties."localRepo.location"
  }

  def setup() {
    buildFile << """
      buildscript {
        repositories {
          maven {
            url "file://${localRepoLocation()}"
            jcenter()
          }
        }
        dependencies {
          classpath "co.freeside:compass-gradle-plugin:1.0.10"
        }
        configurations.all {
          resolutionStrategy.cacheDynamicVersionsFor 0, "seconds"
        }
      }
      apply plugin: "co.freeside.compass"

      dependencies {
        compass "rubygems:compass:+"
      }
    """
  }

  @Ignore("compass fails with no source files")
  def "compile is up to date for an empty sourceset"() {
    given:
    directory("src/main/sass")

    when:
    run COMPILE_TASK_NAME

    then:
    upToDate ":$COMPILE_TASK_NAME"
  }

  def "compiles a basic .scss stylesheet"() {
    given:
    file("src/main/sass/basic.scss") << '''
      $font: Georgia, serif;
      body { font-family: $font; }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with("build/stylesheets/basic.css") { path ->
      fileExists path
      with(stylesheet(path)) {
        item(0).cssText == "body { font-family: Georgia, serif }"
      }
    }
  }

  def "a subsequent execution is up-to-date"() {
    given:
    file("src/main/sass/basic.scss") << 'body { font-family: Georgia, serif; }'

    and:
    run COMPILE_TASK_NAME

    expect:
    fileExists "build/stylesheets/basic.css"

    when:
    run COMPILE_TASK_NAME

    then:
    upToDate ":$COMPILE_TASK_NAME"
  }

  def parser = new CSSOMParser(new SACParserCSS3())

  private CSSRuleList stylesheet(String path) {
    file(path).withReader { r ->
      parser.parseStyleSheet(new InputSource(r), null, null).cssRules
    }
  }

}
