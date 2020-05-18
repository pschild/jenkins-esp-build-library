import groovy.json.JsonSlurper

class EspChoiceBuilder {
    private JsonSlurper slurper = new JsonSlurper()
    private scriptRef

    public EspChoiceBuilder(scriptRef) {
        this.scriptRef = scriptRef
    }
    
    public build() {
        def jsonResponse = this.slurper.parseText(this.scriptRef.libraryResource("esp-config.json"))
        def values = []
        def labels = []
        jsonResponse.each {
            values << it.chipId + "|" + it.pioEnv
            labels << "#" + it.id + " - " + it.model + " - " + it.description.replace(",", "")
        }
        
        def result = []
        result << this.scriptRef.extendedChoice(
            name: 'ESP_TARGETS',
            description: 'Choose for which chips to build',
            visibleItemCount: 50,
            multiSelectDelimiter: ',',
            type: 'PT_CHECKBOX',
            value: values.join(','),
            descriptionPropertyValue: labels.join(',')
        )
        return result
    }
}
