$(function () {
  let currentUrl = window.location.href;

  $('nav a').each((idx, item) => {
    let linkUrl = $(item).attr('href');
    
    if(currentUrl.includes(linkUrl) && linkUrl != '') {
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