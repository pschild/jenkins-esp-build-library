import groovy.json.JsonSlurper

class EspConfigLoader {
    private JsonSlurper slurper = new JsonSlurper()
    private String path

    public EspConfigLoader(String path) {
        this.path = path
    }
    
    public parseIt() {
        def jsonResponse = this.slurper.parseText(libraryResource(this.path))
        def result = []
        jsonResponse.each {
            result << it.chipId + "|" + it.pioEnv
        }

        return result
    }
}
