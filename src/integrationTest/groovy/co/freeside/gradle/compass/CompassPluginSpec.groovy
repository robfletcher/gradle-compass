package co.freeside.gradle.compass

import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import org.gololang.gradle.test.integration.framework.IntegrationSpec
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSRuleList
import spock.lang.Shared

abstract class CompassPluginSpec extends IntegrationSpec {

  @Shared parser = new CSSOMParser(new SACParserCSS3())
  protected final COMPILE_TASK_NAME = "compassCompile"
  protected final CLEAN_TASK_NAME = "compassClean"

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
    """
  }

  protected static String localRepoLocation() {
    System.properties."localRepo.location"
  }

  protected CSSRuleList stylesheet(String path) {
    assert fileExists(path), "file $path does not exist"
    file(path).withReader { r ->
      parser.parseStyleSheet(new InputSource(r), null, null).cssRules
    }
  }
}
