Vue.component('build-progress', {
    template: `<div class="uk-navbar-item uk-width-medium" v-if="progress != maxProgress">
<div class="uk-margin-small-right">{{progressType}}...</div>
<progress class="uk-progress" :value="progress" :max="maxProgress"></progress>
</div>`,
    data: function() {
        return {
            progressType: '',
            progress: 100,
            maxProgress: 100,
            _timer: undefined
        }
    },
    mounted() {
        this.$bus.$on('progress', message => {
            let progressMessage = JSON.parse(message);
            this.progressType = progressMessage.progressType;

            clearTimeout(this._timer);

            if(progressMessage.currentProgress === 0) {
                // set a minimum progress amount to show it is working
                this.progress = Math.ceil(((1/2)*(1/progressMessage.maxProgress))*100); // half the progress to the 1/max, as a percentage
            }
            else if(progressMessage.currentProgress === progressMessage.maxProgress) {
                // set a maximum progress for a brief moment to show it completed, but only if it hasn't already bee set to complete
                if(this.progress !== 100) {
                    this.progress = 99;
                    this._timer = setTimeout(()=>{
                        this.progress = 100;
                    }, 350);
                }
            }
            else {
                this.progress = Math.ceil((progressMessage.currentProgress/progressMessage.maxProgress)*100);
            }
        });
    },
    beforeDestroy() {
        this.$bus.$off('progress');
    },
});


