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
        def values = []
        def labels = []
        jsonResponse.each {
            values << it.chipId + "|" + it.pioEnv
            labels << "#" + it.id + ", " + it.description + " (" + it.model + ")"
        }
        
        def result = []
        result << this.scriptRef.extendedChoice(
            name: 'CHIPS_CHOSEN',
            description: 'Lorem ipsum',
            visibleItemCount: 50,
            multiSelectDelimiter: ';',
            type: 'PT_CHECKBOX',
            value: values.join(';'),
            descriptionPropertyValue: labels.join(';')
        )
        return result
    }
}
