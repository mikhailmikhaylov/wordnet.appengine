var Highlighting = (function ($) {
	var $originalText,
		$processTextButton,
		$textWithHighlighting,
		$unknownWordsList;
		
	var text = '', 
		unknownWords = [],
		clientId = '';
	
	var init = function () {
		$originalText = $('.js-original-text');
		$processTextButton = $('.js-process-text-button');
		$textWithHighlighting = $('.js-text-with-highlighting');
		$unknownWordsList = $('.js-unknown-words-list');
		
		loadDummyText();
		bindService();
		
		attachEventHandlers();
	};
	
	var attachEventHandlers = function () {
		$processTextButton.click(processText);
		$unknownWordsList.change(unknownWordSelected);
	};
	
	var processText = function () {
        text = $originalText.val();

		console.info('Sending data...');

        if (clientId == '') {
            console.error('Not bound yet');
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/enqueue',
            data: 'argument=' + text + '&channel-id=' + clientId
        }).done(function(data) {
            console.info('Task scheduled: ' + data);
            $('#result').val(data);
        }).fail(function() {
            console.error('Error scheduling task');
        });

	};
	
	var unknownWordSelected = function () {
		var words = [];
		
		$.each(this.options, function (index, item) {
			if (item.selected) {
				var matchingWords = unknownWords[index].matchingWords;
				
				matchingWords.forEach(function (word) {
					words.push(word);
				});
			}	
		});
		
		highlightWords(words);
	};
	
	var highlightWords = function (words) {
		var highlightedText = text;
		var wrapper = {
			startTag: '<span class="highlight">',
			endTag: '</span>'
		};
		
		words.sort(function (a, b) {
			return b.position - a.position;	
		});
		
		words.forEach(function (word) { 
			var endIndex = word.position + word.word.length;
			var startIndex = word.position;
			
			highlightedText = highlightedText.slice(0, endIndex) + wrapper.endTag + highlightedText.slice(endIndex);
			highlightedText = highlightedText.slice(0, startIndex) + wrapper.startTag + highlightedText.slice(startIndex);
		});
		
		$textWithHighlighting.html(highlightedText);
	};
	
	var loadDummyText = function () {
		$.get('data/highlighting/text.txt', function (dummyText) {
			$originalText.val(dummyText);
		});
	};

	var bindService = function() {
        $.ajax({
            type: 'POST',
            url: '/bind'
        }).done(function(data) {
            clientId = data;
            console.info('Bound to service as ' + clientId);
            connect(clientId);
        }).fail(function() {
            console.error('Could not bind to service.');
        });
    };

    var connect = function (token) {
        var channel = new goog.appengine.Channel(token);

        var socket = channel.open();

        socket.onopen = function() {
            console.log("Channel opened");
        };
        socket.onmessage = function (data) {
            console.log("Message received.", data);
            processResponse(data);
        };
        socket.onerror = function() {
            console.log("Channel errored");
        };
        socket.onclose = function() {
            console.log("Channel closed");
        };
    };

    var processResponse = function (response) {
        var responseData = JSON.parse(response.data);
        // TODO:SEVERE:mikhail.mikhaylov: TaskId should be handled.
        unknownWords = responseData.response.unknownWords;

        $unknownWordsList.empty();
        $textWithHighlighting.html(text);

        unknownWords.forEach(function (word) {
            var option = document.createElement('option');
            option.value = word.baseForm;
            option.text = word.baseForm;

            $unknownWordsList.append(option);
        });
    };

	return {
		init: init		
	};
})($);

Highlighting.init();