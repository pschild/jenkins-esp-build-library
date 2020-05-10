import groovy.json.JsonSlurper

class EspConfigLoader {
    private JsonSlurper slurper = new JsonSlurper()
    String jsonStr

    public EspConfigLoader(String jsonStr) {
        this.jsonStr = jsonStr
    }
    
    public parseIt() {
        def jsonResponse = this.slurper.parseText(this.jsonStr)
        def result = []
        jsonResponse.each {
            result << it.chipId + "|" + it.pioEnv
        }

        return result
    }
}
