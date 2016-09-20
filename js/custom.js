(function ($) {

	new gnMenu( document.getElementById( 'gn-menu' ) );


	//jQuery for page scrolling feature - requires jQuery Easing plugin
	$(function() {
		$('.gn-menu li a').bind('click', function(event) {
			var $anchor = $(this);
			$('html, body').stop().animate({
				scrollTop: $($anchor.attr('href')).offset().top
			}, 1500, 'easeInOutExpo');
			//event.preventDefault();
		});
		$('a.scroll').bind('click', function(event) {
			var $anchor = $(this);
			$('html, body').stop().animate({
				scrollTop: $($anchor.attr('href')).offset().top
			}, 1500, 'easeInOutExpo');
			event.preventDefault();
		});
	});

	if(window.location.hash){
		$('.gn-menu li a[href=#' + window.location.hash + ']').click();
	}
	
	//nivo lightbox
	$('.gallery-item a').nivoLightbox({
		effect: 'fadeScale',                             // The effect to use when showing the lightbox
		theme: 'default',                           // The lightbox theme to use
		keyboardNav: true,                          // Enable/Disable keyboard navigation (left/right/escape)
		clickOverlayToClose: true,                  // If false clicking the "close" button will be the only way to close the lightbox
		onInit: function(){},                       // Callback when lightbox has loaded
		beforeShowLightbox: function(){},           // Callback before the lightbox is shown
		afterShowLightbox: function(lightbox){},    // Callback after the lightbox is shown
		beforeHideLightbox: function(){},           // Callback before the lightbox is hidden
		afterHideLightbox: function(){},            // Callback after the lightbox is hidden
		onPrev: function(element){},                // Callback when the lightbox gallery goes to previous item
		onNext: function(element){},                // Callback when the lightbox gallery goes to next item
		errorMessage: 'The requested content cannot be loaded. Please try again later.' // Error message when content can't be loaded
	});
	
	$.getJSON('/update2.json', function(response){ 
        	$.getJSON(response.gitlab, function(data){ 
	            $('a.apk-download').text('Download ' + data.name);
	            $('a.apk-download').attr('href', data.assets[0].browser_download_url);
		})
    	});

	
})(jQuery);
