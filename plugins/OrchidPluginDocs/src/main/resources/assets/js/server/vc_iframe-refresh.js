Vue.component('iframe-refresh', {
    template: `
<iframe v-if="ready" :src="src" style="
    width: 100%;
    height: calc(100vh - 80px);
"></iframe>
`,
    data: function() {
        return {
            src: '',
            ready: true
        }
    },
    props: ['src'],
    mounted() {
        this.$bus.$on('buildfinish', message => {
            console.log("build finished, reloading iframe");

            let oldSrc = '' + this.src;
            this.ready = false;
            this.src += '';
            setTimeout(() => {
                this.src = oldSrc;
                this.ready = true;
            }, 1);
        });
    },
    beforeDestroy() {
        this.$bus.$off('buildfinish');
    },
});


