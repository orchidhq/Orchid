Vue.component('enter-command', {
    template: `
    <form v-on:submit.prevent="$bus.$emit('submitCommand', command); command = ''">
        <div class="uk-grid-small uk-grid">
            <div class="uk-width-3-4 uk-position-relative">
                <button type="submit" class="uk-form-icon uk-form-icon-flip" uk-icon="icon: push" ></button>
                <input class="uk-input" type="text" v-model="command" placeholder="Enter Command..." list="commandHistory">
            </div>
            <div class="uk-width-1-4">
                <button type="submit" class="uk-button uk-button-default">Submit</button>
            </div>   
        </div>
        <datalist id="commandHistory">
            <option v-for="item in commandHistory" :value="item">
        </datalist>
    </form>`,
    data: function () {
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

            this.commandHistory.forEach(function (value) {
                console.log(value);
                if (value === command) {
                    hasCommand = true;
                }
            });

            if (!hasCommand) {
                this.commandHistory.unshiftMax(command, this.maxMessageCount);
            }
        });
    },
    beforeDestroy() {
        this.$bus.$off('indexprogress');
        this.$bus.$off('buildprogress');
    },
});


