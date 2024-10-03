$(document).ready(function () {
    let contents =
	`##### 😃 여기는 어떤 곳인가요?\n\n\n\n` +
	`2024년 추석에 재미 삼아 만든 제 개인 홈페이지입니다.\n\n\n\n` +
	`해당 홈페이지는 라즈베리파이 NAS + Docker를 통해 배포되었습니다.\n\n\n\n` +
	`그리고 Spring Boot 3.x .. Thymeleaf를 사용해 만들었어요.\n\n\n\n` +
	`##### 🎈 어떤 글을 올릴 계획인가요?\n` +
	`공부 내용은 주로 기술 블로그에 작성할 것 같고 아마 제 취향의 음악이나 제가 찍은 일상 사진들을 올리지 않을까 싶습니다.\n`;
	
    $('#article-home').html(marked.parse(contents));
	
	if($(window).width() >= '768') {
		if ($('#thumbnail-home-music .thumbnail-item').length < 4) {
		    $('#thumbnail-home-music .thumbnail-contents-wrap').css('justify-content', 'right');
		    // $('#thumbnail-home-music .thumbnail-item').css('margin-left', '10px');
		}
		
		if ($('#thumbnail-home-photo .thumbnail-item').length < 4) {
		    $('#thumbnail-home-photo .thumbnail-contents-wrap').css('justify-content', 'right');
		    // $('#thumbnail-home-photo .thumbnail-item').css('margin-left', '10px');
		}
		
		if ($('#thumbnail-home-daily .thumbnail-item').length < 4) {
		    $('#thumbnail-home-daily .thumbnail-contents-wrap').css('justify-content', 'right');
		    // $('#thumbnail-home-daily .thumbnail-item').css('margin-left', '10px');
		}
	}
});