requirejs.config({
    baseUrl: 'scripts',
    paths: {
        jquery: 'dependencies/jquery-2.1.4'
    }
});

requirejs(['jquery', '/_ah/channel/jsapi'], function($, channels) {
    console.info('Module loaded');
    var state = {};

    var bindService = function(state) {
        $.ajax({
            type: 'POST',
            url: '/bind'
        }).done(function(data) {
            state.clientId = data;
            console.info('Bound to service as ' + state.clientId);
            connect(state.clientId);
        }).fail(function() {
            console.error('Could not bind to service.');
        });
    };

    function connect(token) {
        var channel = new goog.appengine.Channel(token);

        var socket = channel.open();

        socket.onopen = function() {
            console.log("Channel opened");
        };
        socket.onmessage = function (data) {
            console.log("Message received: {}.", data);
        };
        socket.onerror = function() {
            console.log("Channel errored");
        };
        socket.onclose = function() {
            console.log("Channel closed");
        };
    }

    bindService(state);

    $(document).ready(function(){
        $('#submit_button').click(function(e){
            console.info('Sending data...');

            var arg = $('#argument').val();
            if (state.clientId == undefined) {
                console.error('Not bound yet');
                return;
            }

            $.ajax({
                type: 'POST',
                url: '/enqueue',
                data: 'argument=' + arg + '&channel-id=' + state.clientId
            }).done(function(data) {
                console.info('Task scheduled: ' + data);
                $('#result').val(data);
            }).fail(function() {
                console.error('Error scheduling task');
            });

            e.stopPropagation();
            e.preventDefault();

            return false;
        });
    });
});
