String template = new File("${project.basedir}/config/application-template.yml".toString()).getText()


def applicationYmlText = new groovy.text.SimpleTemplateEngine().createTemplate(template)
        .make([
        myhostname: java.net.InetAddress.getLocalHost().getHostName()
])

def pathApplicationYml = "${project.basedir}/config/"
println "writing dir " + pathApplicationYml
new File(pathApplicationYml).mkdirs()

println "writing file"

File applicationYml = new File(pathApplicationYml + "application.yml")

applicationYml.withWriter('UTF-8') { writer ->
    writer.write(applicationYmlText)
}