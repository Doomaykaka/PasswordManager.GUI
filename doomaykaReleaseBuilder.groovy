import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import groovy.cli.commons.CliBuilder;
import java.nio.file.Path
import java.nio.file.Paths
import groovy.io.FileType
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

logger = Logger.getLogger("Logger");

jrePath = null
BUILD_FOLDER = Paths.get("./app/build")

def app(){
	logger.log(Level.INFO, "Hello")

	createCli()

	println(BUILD_FOLDER.toString())
	println(jrePath)

	def outputFolder = Paths.get(BUILD_FOLDER.toString(), "/launch4j")
	println(outputFolder.toString())

	def version = getProjectVersion()
	println(version)

	def projectName = getExecutableName(outputFolder)
	println(projectName)

	logger.log(Level.INFO, """Building ${projectName} v${version}!""")

	copyJre(Paths.get(jrePath), outputFolder)
	copyJar(version, outputFolder)
	copyDocs(outputFolder)

	pathToArchive = createArchive(outputFolder, BUILD_FOLDER)

}

def createCli(){
	def cli = new CliBuilder(usage:'doomaykaReleaseBuilder [options]')
	cli.j(args:1, 'Path to JRE')
	cli.jrePath(args:1, 'Path to JRE')
	cli.h('Help')
	cli.stopAtNonOption=false
	def options = cli.parse(args)

	if(options.h){
		cli.usage()
		System.exit(0)
	}

	if(options.j || options.jrePath){
		if(options.j){
			jrePath = options.j
		}

		if(options.j){
			jrePath = options.j
		}
	} else {
		logger.log(Level.WARNING, "JRE path required")
		System.exit(0)
	}
}

def String getProjectVersion(){
	def findedGroup = null

	Paths.get(BUILD_FOLDER.toString(), "/libs").eachFile(FileType.FILES){
		file ->
			def fileName = new File(file.toString()).name
			def pattern = ~ /app-(\d\.\d\.\d)\.jar/
			res = pattern.matcher(fileName);
  			if(res.matches()){
				findedGroup = res.group(1)
			}
	}

	if(findedGroup){
		return findedGroup
	} else {
		throw new RuntimeException("Failed to parse project version")
	}
}

def getExecutableName(Path outputFolder){
	def findedName = null

	outputFolder.eachFile(FileType.FILES){
		file ->
			def fileName = new File(file.toString()).name
  			if(fileName.endsWith(".exe")){
				findedName = fileName.reverse().replaceFirst(".exe".reverse(),"").reverse()
			}
	}

	if(findedName){
		return findedName
	} else {
		throw new RuntimeException("Failed to parse executable name")
	}
}

def copyJre(Path jrePath, Path outputFolder){
    jreFolderName = new File(jrePath.toString()).name
    jreOutputPath = Paths.get(outputFolder.toString(), jreFolderName)

	logger.log(Level.INFO, """Copying JRE from ${jrePath} to ${jreOutputPath}""")

	def outputDir = new File(jreOutputPath.toString())
	outputDir.mkdir()

	Files.walk(jrePath)
                .forEach(sourcePath -> {
                    try {
                        Path targetPath = outputDir.toPath().resolve(jrePath.relativize(sourcePath));
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
						throw new RuntimeException("Failed to copy JRE: " + ex.getMessage())
                    }
                });
}

def copyJar(String projectVersion, Path outputFolder){
	def libDir = new File(Paths.get(outputFolder.toString(), "lib").toString())
	logger.log(Level.INFO, """Removing ${libDir.toPath().toString()}""")

	Files.walkFileTree(libDir.toPath(),
      new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult postVisitDirectory(
          Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(
          Path file, BasicFileAttributes attrs)
          throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }
    });

	libDir.mkdir()

	def jarFullPath = Paths.get(BUILD_FOLDER.toString(), "libs", """app-${projectVersion}.jar""")
	logger.log(Level.INFO, """Copying ${jarFullPath} to ${libDir.toPath()}""")

	Files.copy(jarFullPath, Paths.get(libDir.toString(), """app-${projectVersion}.jar"""), StandardCopyOption.REPLACE_EXISTING)
}

def copyDocs(Path outputFolder){
	logger.log(Level.INFO, """Copying docs to ${outputFolder.toString()}""")
    Files.copy(Paths.get(".", "README.md"), Paths.get(outputFolder.toString(), "README.md"), StandardCopyOption.REPLACE_EXISTING)
}

def createArchive(Path sourceFolder, Path targetFolder){
	logger.log(Level.INFO, "Creating archive")

	zipFile = new File(Paths.get(targetFolder.toString(), "Release.zip").toString())
	zipFile.createNewFile()

	ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))
    sourceFolder.eachFileRecurse({
        if((new File(it.toString())).isFile()) {
			zos.putNextEntry(
				new ZipEntry(
					it.toString().replace(".\\app\\build\\launch4j\\","")
				)
			)
			zos << it.bytes
		}
        zos.closeEntry()
    })
    zos.close()

	return zipFile
}

def makeRelease(){
	logger.log(Level.INFO, "Releasing!")


}



app()
