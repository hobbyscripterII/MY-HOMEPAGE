$(document).ready(() => {
	let $body = $('body');
	let $headerSidebar = $('#header-sidebar');
	let $overlay = $('.overlay');
	let headerHamburgerBtn = $('#header-hamburger-btn');
	
	function toggleSidebar() {
		$headerSidebar.toggleClass('open');
		$overlay.css('display', ($headerSidebar.hasClass('open') ? 'flex' : 'none'));
		$body.css('overflow', ($headerSidebar.hasClass('open') ? 'hidden' : 'auto'));
	}

	headerHamburgerBtn.on('click', toggleSidebar);

	$overlay.on('click', () => {
		toggleSidebar();
	})
})