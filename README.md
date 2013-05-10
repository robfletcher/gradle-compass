# Gradle Compass plugin

A [SASS][sass] / [Compass][compass] plugin for [Gradle][gradle]. The plugin uses JRuby to install and run Compass.

## Tasks

The plugin adds the following tasks:

### compileSass

Compiles all SASS files. Equivalent to the `compass compile` command. The task supports incremental build.

### watchSass

Compiles and watches all SASS files. Equivalent to the `compass watch` command. The task runs in the background so it can be added to the dependency chain for the task that starts your application.

### installCompass

Installs the Compass Ruby gem. This is executed automatically by the `compileSass` and `watchSass` tasks.

## Configuration

You need to specify the target directory where compiled CSS files should go and the source directories containing SASS/SCSS files, images and JavaScript. For example:

	compass {
		cssDir = file('public/styles')
		sassDir = file('src/main/sass')
		imagesDir = file('public/images')
		javascriptsDir = file('public/scripts')
	}

These are equivalent to the `--css-dir`, `--sass-dir`, `--images-dir` and `--javascripts-dir` command line options for Compass.

You can also specify the directory where the Compass Ruby gem will be installed using `gemPath`. By default this is `.jruby/gems` inside the Gradle project directory.

## Using with other tasks

You will typically want to execute the Compass tasks as part of a larger build. For example this configuration will run `compileSass` as part of `processResources`, `watchSass` as part of the Application plugin's `run` task and clean the output of `compileSass` as part of `clean`:

	processResources.inputs.files compileSass
	run.dependsOn watchSass
	clean.dependsOn cleanCompileSass
