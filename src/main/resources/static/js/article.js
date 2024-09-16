$(document).ready(function () {
    let contents =
	`### 😃 여기는 어떤 곳인가요?\n\n` +
	`추석에 재미 삼아 만든 제 개인 홈페이지입니다.\n\n` +
	`해당 홈페이지는 라즈베리파이 NAS + Docker를 통해 배포되었습니다.\n\n` +
	`그리고 Spring Boot 3.x .. Thymeleaf를 사용해 만들었어요.\n\n` +
	`### 😎 홈페이지를 만들게 된 계기가 있나요?\n\n` +
	`[visla magazine](https://visla.kr/)에 영감을 받아 만들게 되었습니다.\n\n` +
	`클론코딩은 아니지만 비슷한 부분이 많이 있을꺼에요..\n\n` +
	`### 🎈 어떤 글을 올릴 계획인가요?\n\n` +
	`공부 내용은 주로 기술 블로그에 작성할 것 같고 아마 제 취향의 음악이나 제가 찍은 일상 사진들을 올리지 않을까 싶습니다.\n\n` +
	`### 🧒 소개 끝!\n\n`;
	
    $('#article-home').html(marked.parse(contents));
});