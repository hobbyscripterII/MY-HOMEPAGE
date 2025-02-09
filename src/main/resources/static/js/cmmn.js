$(function () {
  let currentUrl = window.location.pathname + window.location.search;
  
  $('nav a').each((idx, item) => {
    let linkUrl = $(item).attr('href');
    
	if(currentUrl == linkUrl) {
      $(item).addClass('active');
	}
  });
})