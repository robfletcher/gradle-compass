package com.github.robfletcher.compass

import com.github.jengelman.gradle.plugins.processes.tasks.JavaFork
import com.github.jrubygradle.api.gems.GemUtils
import com.github.jrubygradle.api.gems.GemOverwriteAction
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

import static com.github.jrubygradle.api.gems.GemOverwriteAction.SKIP
import static com.github.jrubygradle.api.gems.GemOverwriteAction.OVERWRITE

class CompassFork extends JavaFork implements CompassTaskOptions {

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
  File gemDir

  @TaskAction @Override void javafork() {
    def gemDir = getGemDir()

    gemDir.mkdirs()
    Configuration jrubyConfig = project.extensions.jruby.jrubyConfiguration
    GemUtils.extractGems(
      project,
      jrubyConfig,
      project.configurations.getByName("compass"),
      gemDir,
      overwriteGems()
    )

    setMain "org.jruby.Main"
    environment GEM_HOME: gemDir, PATH: getComputedPATH(gemDir)
    classpath JRubyExecUtils.classpathFromConfiguration(jrubyConfig)
    args JRubyExecUtils.buildArgs(["-S"], new File("compass"), ScriptArgumentsBuilder.compassArgs(this))

    super.javafork()
  }

  String getComputedPATH(File gemDir) {
    def path = new File(gemDir, "bin")
    path.absolutePath + File.pathSeparatorChar + System.getenv().PATH
  }

  private GemOverwriteAction overwriteGems() {
    project.gradle.startParameter.refreshDependencies ? OVERWRITE : SKIP
  }
}
