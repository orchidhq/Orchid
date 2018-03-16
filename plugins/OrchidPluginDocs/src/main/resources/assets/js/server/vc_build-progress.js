Vue.component('build-progress', {
    template: `<div class="uk-navbar-item uk-width-medium" v-if="progress != maxProgress">
<div class="uk-margin-small-right">{{progressType}}...</div>
<progress class="uk-progress" :value="progress" :max="maxProgress"></progress>
</div>`,
    data: function() {
        return {
            progressType: 'indexing',
            progress: 0,
            maxProgress: 0
        }
    },
    mounted() {
        this.$bus.$on('indexprogress', message => {
            let progressMessage = message.split("/");
            this.progressType = 'Indexing';
            this.progress = parseInt(progressMessage[0].trim());
            this.maxProgress = parseInt(progressMessage[1].trim());
        });
        this.$bus.$on('buildprogress', message => {
            let progressMessage = message.split("/");
            this.progressType = 'Building';
            this.progress = parseInt(progressMessage[0].trim());
            this.maxProgress = parseInt(progressMessage[1].trim());
        });
    },
    beforeDestroy() {
        this.$bus.$off('indexprogress');
        this.$bus.$off('buildprogress');
    },
});


