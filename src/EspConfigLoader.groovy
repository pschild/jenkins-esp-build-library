import groovy.json.JsonSlurper

class EspConfigLoader {
    private JsonSlurper slurper = new JsonSlurper()
    private String path
    Script script

    public EspConfigLoader(String path) {
        this.path = path
    }
    
    public parseIt() {
        def jsonResponse = this.slurper.parseText(script.libraryResource(this.path))
        def result = []
        jsonResponse.each {
            result << it.chipId + "|" + it.pioEnv
        }

        return result
    }
}
