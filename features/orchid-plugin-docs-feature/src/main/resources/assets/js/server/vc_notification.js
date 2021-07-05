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
            var params = JSON.parse(message);
            if(params.success) {
                UIkit.notification({
                    message: 'Deploy complete.',
                    status: 'success',
                    pos: 'bottom-right',
                });
            }
            else {
                UIkit.notification({
                    message: 'Deploy failed.',
                    status: 'danger',
                    pos: 'bottom-right',
                });
            }
        });
    },
    beforeDestroy() {
        this.$bus.$off('buildfinish');
        this.$bus.$off('deployfinish');
    },
});


