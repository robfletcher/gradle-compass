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
    new ScriptArgumentsBuilder(this)
        .addFlag("--sourcemap", isSourcemap())
        .addFlag("--time", isTime())
        .addFlag("--debug-info", isDebugInfo())
        .addGems("--require", project.configurations.getByName(CONFIGURATION_NAME).dependencies)
        .addFile("--load", getLoad())
        .addFile("--load-all", getLoadAll())
        .addDirs("--import-path", getImportPath())
        .addFlag("--quiet", isQuiet())
        .addFlag("--trace", isTrace())
        .addFlag("--force", isForce())
        .addFlag("--boring", isBoring())
        .addFile("--config", getConfig())
        .addFile("--sass-dir", getSassDir())
        .addFile("--css-dir", getCssDir())
        .addFile("--images-dir", getImagesDir())
        .addFile("--javascripts-dir", getJavascriptsDir())
        .addFile("--fonts-dir", getFontsDir())
        .addString("--environment", getEnv())
        .addString("--output-style", getOutputStyle())
        .addFlag("--relative-assets", isRelativeAssets())
        .addFlag("--no-line-comments", isNoLineComments())
        .addString("--http-path", getHttpPath())
        .addString("--generated-images-path", getGeneratedImagesPath())
        .toArgumentList() + super.scriptArgs()
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}
