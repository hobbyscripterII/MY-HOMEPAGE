$(function () {
  let currentUrl = window.location.pathname + window.location.search;
  console.log(`currentUrl = ${currentUrl}`);
  
  $('nav a').each((idx, item) => {
    let linkUrl = $(item).attr('href');
    
	console.log(`linkUrl = ${linkUrl}`);
	
	if(currentUrl == linkUrl) {
      $(item).addClass('active');
	}
  });

  $(window).scroll(function () {
    let $winTop = $(window).scrollTop();
    let $lastScrollTop = 0;
    
    if ($winTop > $lastScrollTop) {
      $("body").addClass("sticky-header");
    } else {
      $("body").removeClass("sticky-header");
    }
  });
})