var Highlighting = (function ($) {
	var $originalText,
		$processTextButton,
		$textWithHighlighting,
		$unknownWordsList;
		
	var text = '', 
		unknownWords = [];
	
	var init = function () {
		$originalText = $('.js-original-text');
		$processTextButton = $('.js-process-text-button');
		$textWithHighlighting = $('.js-text-with-highlighting');
		$unknownWordsList = $('.js-unknown-words-list');
		
		loadDummyText();
		
		attachEventHandlers();
	};
	
	var attachEventHandlers = function () {
		$processTextButton.click(processText);
		$unknownWordsList.change(unknownWordSelected);
	};
	
	var processText = function () {
		text = $originalText.val();
		
		$.get('data/highlighting/text-processing-result.json?text=' + text , function (response) {
			unknownWords = response.unknownWords;
			
			$unknownWordsList.empty();
			$textWithHighlighting.html(text);
			
			unknownWords.forEach(function (word) {
				var option = document.createElement('option');
				option.value = word.baseForm;
				option.text = word.baseForm;
				
				$unknownWordsList.append(option);
			});		
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
	
	return {
		init: init		
	};
})($);

Highlighting.init();