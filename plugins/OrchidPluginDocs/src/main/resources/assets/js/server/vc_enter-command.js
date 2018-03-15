Vue.component('enter-command', {
    template: `
    <form v-on:submit.prevent="$bus.$emit('submitCommand', command); command = ''">
        <button type="submit" class="uk-form-icon uk-form-icon-flip" uk-icon="icon: push" ></button>
        <input class="uk-input" type="text" v-model="command" placeholder="Enter Command..." list="commandHistory">
        <datalist id="commandHistory">
            <option v-for="item in commandHistory" :value="item">
        </datalist>
    </form>`,
    data: function() {
        return {
            command: '',
            commandHistory: [],
            maxHistoryCount: 25
        }
    },
    props: {
        maxHistoryCount: {
            type: Number,
            default: 25
        },
    },
    mounted() {
        this.$bus.$on('submitCommand', command => {
            let hasCommand = false;

            this.commandHistory.forEach(function(value) {
                console.log(value);
                if(value === command) {
                    hasCommand = true;
                }
            });

            if(!hasCommand) {
                this.commandHistory.unshiftMax(command, this.maxMessageCount);
            }
        });
    },
    beforeDestroy() {
        this.$bus.$off('indexprogress');
        this.$bus.$off('buildprogress');
    },
});


