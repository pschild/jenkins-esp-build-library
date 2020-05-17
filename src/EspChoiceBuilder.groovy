import groovy.json.JsonSlurper

class EspChoiceBuilder {
    private JsonSlurper slurper = new JsonSlurper()
    private String jsonStr
    private scriptRef

    public EspChoiceBuilder(scriptRef, String jsonStr) {
        this.scriptRef = scriptRef
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
        def labels = []
        jsonResponse.each {
            labels << it.chipId + "|" + it.pioEnv
        }
        
        def result = []
        result << this.scriptRef.extendedChoice(
            name: 'CHIPS_CHOSEN',
            description: 'Lorem ipsum',
            visibleItemCount: 50,
            multiSelectDelimiter: ',',
            type: 'PT_CHECKBOX',
            value: [1,2,3].join(','),
            description: ['A','B','C'].join(',')
        )
        return result
    }
}
