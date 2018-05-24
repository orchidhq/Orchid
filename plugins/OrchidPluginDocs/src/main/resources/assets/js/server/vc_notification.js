Vue.component('notifications', {
    template: `<div></div>`,
    data: function() {
        return {}
    },
    props: [],
    mounted() {
        this.$bus.$on('buildfinish', message => {
            UIkit.notification({
                message: 'Build complete.',
                status: 'success',
                pos: 'bottom-right',
            });
        });
        this.$bus.$on('deployfinish', message => {
            UIkit.notification({
                message: 'Deploy complete.',
                status: 'success',
                pos: 'bottom-right',
            });
        });
    },
    beforeDestroy() {
        this.$bus.$off('buildfinish');
        this.$bus.$off('deployfinish');
    },
});


