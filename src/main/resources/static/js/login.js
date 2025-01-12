let $form = $('#form');
let $id   = $('#id');
let $pw   = $('#pw');

$('button').click(function() {
	if ($id.val() == '') { alert('아이디를 입력하세요.'); return; }
	else if ($pw.val() == '') { alert('패스워드를 입력하세요.'); return; }
	else { $form.attr('method', 'post'); $form.submit(); }
});