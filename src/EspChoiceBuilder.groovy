import groovy.json.JsonSlurper

class EspChoiceBuilder {
    private JsonSlurper slurper = new JsonSlurper()
    String jsonStr

    public EspChoiceBuilder(String jsonStr) {
        this.jsonStr = jsonStr
    }
    
    /*public build() {
        def jsonResponse = this.slurper.parseText(this.jsonStr)
        def result = []
        jsonResponse.each {
            result << it.chipId + "|" + it.pioEnv
        }

        return result
    }*/
    
    public build() {
        def jsonResponse = this.slurper.parseText(this.jsonStr)
        def result = []
        result << extendedChoice(
            name: 'CHIPS_CHOSEN',
            description: 'Lorem ipsum',
            visibleItemCount: 50,
            multiSelectDelimiter: ',',
            type: 'PT_CHECKBOX',
            groovyScript: '''
                import groovy.json.JsonSlurper
                return [1,2,3]
            ''',
            descriptionGroovyScript: '''
                import groovy.json.JsonSlurper
                return ['A','B','C']
            '''
        )
        return result
    }
}
