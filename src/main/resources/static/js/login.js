$(document).ready(function () {
    const form = $('#loginForm');
    const errorMessage = $('#error-message');

    form.on('submit', function (event) {
        event.preventDefault();

        const formData = form.serializeArray();
        const data = {};
        formData.forEach(field => {
            data[field.name] = field.value;
        });

        $.ajax({
            url: '/api/manager/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                window.location.href = '/manager';
            },
            error: function (xhr, status, error) {
                console.error('로그인 요청 중 오류 발생:', error);
                errorMessage.text('로그인 실패');
                errorMessage.show();
            }
        });
    });
});
