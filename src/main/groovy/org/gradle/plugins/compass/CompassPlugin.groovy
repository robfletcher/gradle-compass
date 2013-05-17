package org.gradle.plugins.compass

import org.gradle.api.*
import java.nio.charset.*

class CompassPlugin implements Plugin<Project> {

	public static final CONFIGURATION_NAME = 'compass'
	public static final DEFAULT_JRUBY_DEPENDENCY = 'org.jruby:jruby-complete:1.7.3'

	private Project project
	private CompassExtension extension

	void apply(Project project) {
		this.project = project

		createConfiguration()

		def installCompass = project.task('installCompass', type: InstallGems) {
			gems = 'compass'
		}
		def compileSass = project.task('compileSass', type: CompassTask) {
			background = false
			command = "compile"
		}
		def watchSass = project.task('watchSass', type: CompassTask) {
			background = true
			command = "watch"
			outputs.upToDateWhen { false }
		}

		compileSass.dependsOn(installCompass)
		watchSass.dependsOn(installCompass)

		createExtension()
		configureTaskRule()
	}

	private void createConfiguration() {
		project.configurations.create(CONFIGURATION_NAME)
		def config = project.configurations[CONFIGURATION_NAME]
		if (config.dependencies.empty) {
			project.dependencies.add(CONFIGURATION_NAME, DEFAULT_JRUBY_DEPENDENCY)
		}
	}

	private void createExtension() {
		extension = project.extensions.create('compass', CompassExtension)
		extension.with {
			encoding = Charset.defaultCharset().name()
			gemPath = project.file('.jruby/gems')
			cssDir = project.file('build/css')
			sassDir = project.file('src/main/sass')
			imagesDir = project.file('src/main/images')
			javascriptsDir = project.file('src/main/scripts')

      File defaultFontDir = new File('src/main/fonts')
      if (defaultFontDir.exists()) {
        fontsDir = project.file(defaultFontDir)
      }

      projectType = 'stand_alone'
      environment = 'development'
      outputStyle = 'compact'
      debugInfo = true
		}
	}

	private void configureTaskRule() {
		project.tasks.withType(JRubyTask) { JRubyTask task ->
			task.conventionMapping.with {
				encoding = { extension.encoding }
				gemPath = { extension.gemPath }
			}
		}
		project.tasks.withType(CompassTask) { CompassTask task ->
			task.conventionMapping.with {
				cssDir = { extension.cssDir }
				sassDir = { extension.sassDir }
				imagesDir = { extension.imagesDir }
				javascriptsDir = { extension.javascriptsDir }
				relativeAssets = { extension.relativeAssets }
        projectType = { extension.projectType }
        environment = { extension.environment }
        outputStyle = { extension.outputStyle }
        fontsDir = { extension.fontsDir }
        noLineComments = { extension.noLineComments }
        debugInfo = { extension.debugInfo }
        quiet = { extension.quiet }
        trace = { extension.trace }
        force = { extension.force }
        dryRun = { extension.dryRun }
        boring = { extension.boring }
			}
		}
	}

}
