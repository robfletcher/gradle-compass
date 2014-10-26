package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

import static co.freeside.gradle.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyExec {

  String command

  boolean sourcemap
  boolean time
  boolean debugInfo
  @InputDirectory @Optional File load
  @InputDirectory @Optional File loadAll
  @InputFiles @Optional FileCollection importPath
  boolean quiet
  boolean trace
  boolean force
  boolean boring
  @InputFile @Optional File config
  @InputDirectory File sassDir
  @OutputDirectory File cssDir
  @InputDirectory @Optional File imagesDir
  @InputDirectory @Optional File javascriptsDir
  @InputDirectory @Optional File fontsDir
  String outputStyle
  boolean relativeAssets
  boolean noLineComments
  String httpPath
  String generatedImagesPath

  CompassTask() {
    script = new File("compass")
    configuration = CONFIGURATION_NAME
  }

  @Override
  List<String> jrubyArgs() {
    def jrubyArgs = super.jrubyArgs()
    if (!jrubyArgs.contains("-S")) {
      jrubyArgs << "-S"
    }
    return jrubyArgs
  }

  @Override
  List<String> scriptArgs() {
    def scriptArgs = [command]

    if (isSourcemap()) {
      scriptArgs << "--sourcemap"
    }
    if (isTime()) {
      scriptArgs << "--time"
    }
    if (isDebugInfo()) {
      scriptArgs << "--debug-info"
    }
    project.configurations.getByName(CONFIGURATION_NAME).dependencies.each {
      if (it.name != "compass") {
        scriptArgs << "--require" << it.name
      }
    }
    if (getLoad()) {
      scriptArgs << "--load" << getLoad().path
    }
    if (getLoadAll()) {
      scriptArgs << "--load-all" << getLoadAll().path
    }
    for (File importDir in getImportPath()?.files) {
      scriptArgs << "--import-path" << importDir.path
    }
    if (isQuiet()) {
      scriptArgs << "--quiet"
    }
    if (isTrace()) {
      scriptArgs << "--trace"
    }
    if (isForce()) {
      scriptArgs << "--force"
    }
    if (isBoring()) {
      scriptArgs << "--boring"
    }
    if (getConfig()) {
      scriptArgs << "--config" << getConfig().path
    }
    scriptArgs << "--sass-dir" << getSassDir().path
    scriptArgs << "--css-dir" << getCssDir().path
    if (getImagesDir()) {
      scriptArgs << "--images-dir" << getImagesDir().path
    }
    if (getJavascriptsDir()) {
      scriptArgs << "--javascripts-dir" << getJavascriptsDir().path
    }
    if (getFontsDir()) {
      scriptArgs << "--fonts-dir" << getFontsDir().path
    }
    if (getOutputStyle()) {
      scriptArgs << "--output-style" << getOutputStyle()
    }
    if (isRelativeAssets()) {
      scriptArgs << "--relative-assets"
    }
    if (isNoLineComments()) {
      scriptArgs << "--no-line-comments"
    }
    if (getHttpPath()) {
      scriptArgs << "--http-path" << getHttpPath()
    }
    if (getGeneratedImagesPath()) {
      scriptArgs << "--generated-images-path" << getGeneratedImagesPath()
    }

    scriptArgs.addAll(super.scriptArgs())
    return scriptArgs
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}
