Object.defineProperty(Vue.prototype, '$bus', {
    get() {
        return this.$root.bus;
    }
});

Object.defineProperty(Array.prototype, "pushMax", {
    configurable: false,
    enumerable: false,
    writable: false,
    value: function (value, max) {
        if (this.length >= max) {
            this.splice(0, this.length - max + 1);
        }
        return this.push(value);
    }
});

Object.defineProperty(Array.prototype, "unshiftMax", {
    configurable: false,
    enumerable: false,
    writable: false,
    value: function (value, max) {
        if (this.length >= max) {
            this.splice(0, this.length - max + 1);
        }
        return this.unshift(value);
    }
});

let bus = new Vue();
new Vue({
    el: '#orchid-app',
    data: {
        bus: bus,
    },
    created: function () {
        let $bus = this.$bus;

        // setup websocket
        var websocketUrl = window.websocketUrl;
        window.socket = new WebSocket(websocketUrl);
        window.socket.onopen = function (event) {
        };
        window.socket.onclose = function (event) {
        };
        window.socket.onerror = function (error) {
        };
        window.socket.onmessage = function (event) {
            let eventTypeRegex = /^Event type=\[(.*)]:([\s\S]*)/i;
            let message = event.data;
            if (eventTypeRegex.test(message)) {
                let matches = eventTypeRegex.exec(message);
                let name = matches[1].trim();
                let data = matches[2].trim();

                // emit the specific event, and then emit the generic event so we can catch all Orchid events
                $bus.$emit(name, data);
                $bus.$emit("orchidEvent", {name: name, data: data});
            }
        };

        // listen for submitCommand event, so other components can easily communicate with the Orchid server
        this.$bus.$on('submitCommand', command => {
            if (window.socket) {
                window.socket.send(command);
            }
        });
    }
});