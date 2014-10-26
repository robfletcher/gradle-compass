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
  String env
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

    project.configurations.getByName(CONFIGURATION_NAME).dependencies.each {
      if (it.name != "compass") {
        scriptArgs << "--require" << it.name
      }
    }

    scriptArgs.addAll new ScriptArgumentsBuilder(this)
        .add("--sourcemap", isSourcemap())
        .add("--time", isTime())
        .add("--debug-info", isDebugInfo())
        .add("--load", getLoad())
        .add("--load-all", getLoadAll())
        .add("--import-path", getImportPath())
        .add("--quiet", isQuiet())
        .add("--trace", isTrace())
        .add("--force", isForce())
        .add("--boring", isBoring())
        .add("--config", getConfig())
        .add("--sass-dir", getSassDir())
        .add("--css-dir", getCssDir())
        .add("--images-dir", getImagesDir())
        .add("--javascripts-dir", getJavascriptsDir())
        .add("--fonts-dir", getFontsDir())
        .add("--environment", getEnv())
        .add("--output-style", getOutputStyle())
        .add("--relative-assets", isRelativeAssets())
        .add("--no-line-comments", isNoLineComments())
        .add("--http-path", getHttpPath())
        .add("--generated-images-path", getGeneratedImagesPath())
        .toArgumentList()

    scriptArgs.addAll(super.scriptArgs())
    return scriptArgs
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}
