(function($) {
    $(function () {
        window.buildingIndicatorParent = $('#workingIndicator');
        window.buildingIndicator = $('#workingIndicator .progress-bar');
        window.socketMessages = $('#timeline');
        window.eventsList = $('#timeline .eventsList');
        window.commandHistory = $('datalist#command-history');

        function createWebsocketConnection() {
            var websocketUrl = window.websocketUrl;

            window.socket = new WebSocket(websocketUrl);
            window.socket.onopen  = function (event) {};
            window.socket.onclose = function (event) {};
            window.socket.onerror = function (error) {
                console.log('WebSocket Error: ' + error);
            };
            window.socket.onmessage = function (event) {
                var eventTypeRegex = /^Event type=\[(.*)]:([\s\S]*)/i;
                var message = event.data;
                if (eventTypeRegex.test(message)) {
                    var matches = eventTypeRegex.exec(message);
                    handleEvent(matches[1], matches[2]);
                }
            };
        }

        $("#messageform").submit(function (e) {
            e.preventDefault();
            submitCommand();
            return false;
        });

        $("#messageform .btn.btn-secondary").click(function (e) {
            e.preventDefault();
            submitCommand();
            return false;
        });

        $('#timeline .eventsClearButton').click(function (e) {
            e.preventDefault();
            window.eventsList.html('');
            return false;
        });

        function submitCommand() {
            console.log("Submit command");
            if (window.socket) {
                var message = document.getElementById('message').value;
                window.socket.send(message);
                addMessage(true, message);
                document.getElementById('message').value = '';

                var hasCommand = false;
                window.commandHistory.children().each(function(i) {
                    console.log($(this).val());
                    if($(this).val() === message) {
                        hasCommand = true;
                    }
                });

                if(!hasCommand) {
                    commandHistory.prepend('<option value="' + message + '">');
                }
            }
            else {
                addMessage(true, 'Websocket connection is not open', 'danger');
            }
        }

        function addMessage(wasSent, message, colorClass) {
            var calloutClass = (colorClass) ? colorClass : ((wasSent) ? 'warning' : 'primary');
            var calloutName = (wasSent) ? 'Sent' : 'Received';

            var maxNumberOfElements = 100;
            var numberOfElements = window.eventsList.children().length;

            if (numberOfElements > maxNumberOfElements) {
                window.eventsList.children().slice(maxNumberOfElements - numberOfElements).remove();
            }

            window.eventsList.prepend('<div class="callout callout-' + calloutClass + ' m-0 py-3">' +
                '<div>Event <strong>' + calloutName + '</strong></div>' +
                '<small class="text-muted mr-3">' + message + '</small>' +
                '</div><hr class="mx-3 my-0">');
        }

        function setProgress(currentProgress, maxProgress, colorClass) {
            currentProgress = parseInt(currentProgress);
            maxProgress = parseInt(maxProgress);

            var progressPercentage = Math.min(Math.round((currentProgress / maxProgress) * 100), 100);

            if (progressPercentage >= 0 && progressPercentage < 100) {
                window.buildingIndicator.show();

                window.buildingIndicator.removeClass('bg-success');
                window.buildingIndicator.removeClass('bg-info');
                window.buildingIndicator.removeClass('bg-warning');
                window.buildingIndicator.removeClass('bg-danger');
                window.buildingIndicator.addClass('bg-' + colorClass);

                window.buildingIndicator.css('width', progressPercentage + '%');
                window.buildingIndicator.attr('aria-valuenow', currentProgress);
                window.buildingIndicator.attr('aria-valuemax', maxProgress);
                window.buildingIndicatorParent.css('background-color', '#f0f3f5');
            }
            else {
                window.buildingIndicator.hide();

                window.buildingIndicator.css('width', progressPercentage + '%');
                window.buildingIndicator.attr('aria-valuenow', currentProgress);
                window.buildingIndicator.attr('aria-valuemax', maxProgress);
                window.buildingIndicatorParent.css('background-color', '#e4e5e6');
            }
        }

        createWebsocketConnection();

        $('#availableOptions .admin-list-header').click(function (e) {
            e.preventDefault();

            var $listKey = $(this).data('list-key');
            var $selector = '#availableOptions .admin-list-body[data-list-key="' + $listKey + '"]';

            $($selector).toggle();

        });

        function handleEvent(eventType, message) {
            switch (eventType) {
                case 'describe': handleDescribeEvent(message); break;
                case 'indexprogress': handleIndexProgressEvent(message); break;
                case 'buildprogress': handleBuildProgressEvent(message); break;
                default: addMessage(false, message); break;
            }
        }

        function handleDescribeEvent(message) {
            $("#description-container").show();
            $("#description-container-content").html(message);
        }

        function handleIndexProgressEvent(message) {
            var progress = message.split("/");

            setProgress(progress[0].trim(), progress[1].trim(), 'warning');
            addMessage(false, 'Indexing Progress: ' + progress[0].trim() + "/" + progress[1].trim(), 'warning');
        }

        function handleBuildProgressEvent(message) {
            var progress = message.split("/");
            setProgress(progress[0].trim(), progress[1].trim(), 'info');

            var millis = progress[2].trim();
            if (millis > 0) {
                addMessage(false, 'Building Progress: ' + progress[0].trim() + "/" + progress[1].trim() + ' (took ' + millis + 'ms)', 'info');
            }
            else {
                addMessage(false, 'Building Progress: ' + progress[0].trim() + "/" + progress[1].trim(), 'info');
            }
        }
    });
})(jQuery);