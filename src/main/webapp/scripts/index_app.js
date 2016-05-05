requirejs.config({
    baseUrl: 'scripts',
    paths: {
        jquery: 'dependencies/jquery-2.1.4'
    }
});

requirejs(['jquery'], function($) {
    console.info('Module loaded');
    var state = {};

    var bindService = function(state) {
        $.ajax({
            type: 'POST',
            url: '/bind'
        }).done(function(data) {
            state.clientId = data;
            console.info('Bound to service as ' + state.clientId);
        }).fail(function() {
            console.error('Could not bind to service.');
        });
    };

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
                console.info('Data processing returned result');
                $('#result').val(data);
            }).fail(function() {
                console.error('Error processing data');
            });

            e.stopPropagation();
            e.preventDefault();

            return false;
        });
    });
});
