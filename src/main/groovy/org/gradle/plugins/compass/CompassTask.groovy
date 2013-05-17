package org.gradle.plugins.compass

import org.gradle.api.tasks.*
import static org.gradle.plugins.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyTask {

	String command
	boolean background

	boolean relativeAssets
  boolean boring
  boolean debugInfo
  boolean dryRun
  boolean force
  boolean noLineComments
  boolean quiet
  boolean trace

  String environment
  String outputStyle
  String projectType

	@InputDirectory
	File gemPath

	@OutputDirectory
	File cssDir

  @InputDirectory
  @Optional
  File fontsDir

	@InputDirectory
	File sassDir

	@InputDirectory
	File imagesDir

	@InputDirectory
	File javascriptsDir

	CompassTask() {
		doFirst {
			getCssDir().mkdirs()
		}
	}

	@Override
	protected Iterable<String> getJRubyArguments() {
		def args = []
		args << '-X-C'
		args << '-S' << 'compass' << command
		args << '--sass-dir' << getSassDir()
		args << '--css-dir' << getCssDir()
		args << '--images-dir' << getImagesDir()
		args << '--javascripts-dir' << getJavascriptsDir()
		if (getRelativeAssets()) {
			args << '--relative-assets'
		}

    args << '--app' << getProjectType()
    args << '--environment' << getEnvironment()
    args << '--output-style' << getOutputStyle()

    if (getDebugInfo()) {
      args << '--debug-info'
    } else {
      args << '--no-debug-info'
    }

    if (getBoring()) { args << '--boring' }
    if (getDryRun()) { args << '--dry-run' }
    if (getFontsDir()) { args << '--fonts-dir' << getFontsDir() }
    if (getForce()) { args << '--force' }
    if (getNoLineComments()) { args << '--no-line-comments' }
    if (getQuiet()) { args << '--quiet' }
    if (getTrace()) { args << '--trace' }

		return args
	}

	@TaskAction
	@Override
	void jrubyexec() {
		if (background) {
			Thread.start {
				super.jrubyexec()
			}
		} else {
			super.jrubyexec()
		}
	}
}