Vue.component('event-list', {
    template: '<div>' +
    '<ul class="uk-list uk-list-divider">' +
    '<li v-if="messages.length == 0">No activity</li>' +
    '<li v-for="msg in messages">{{msg}}</li>' +
    '</ul></div>',
    data: function() {
        return {
            messages: [],
            maxMessageCount: 25
        }
    },
    props: {
        maxMessageCount: {
            type: Number,
            default: 25
        },
    },
    mounted() {
        this.$bus.$on('orchidEvent', orchidEvent => {
            this.messages.unshiftMax(orchidEvent.name + ": " + orchidEvent.data, this.maxMessageCount);
        })
    },
    beforeDestroy() {
        this.$bus.$off('orchidEvent');
    },
});
