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
})